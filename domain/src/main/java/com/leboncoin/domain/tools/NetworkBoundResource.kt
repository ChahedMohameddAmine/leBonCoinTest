package com.leboncoin.domain.tools


import com.leboncoin.domain.tools.state.ConnectionState
import com.leboncoin.domain.tools.state.GCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


/**
 * GENERIC TYPES
 * This is a generic function and that means it can work with any type of data,
 * @param ResultType is the data type loaded the local cache . Can be any thing, a list or any object.
 * @see networkBoundResource
 *
 * ARGUMENT PARAMETERS
 * This function takes in four argument parameters which are functions
 *
 * NOTE!!! -> all the parameters are function implementations of the following pieces of logic
 *
 * @param query
 * @return Flow<ResultType>
 * pass in a function that loads data from your local cache and returns a flow of your specified data type <ResultType>
 *
 * @param fetch
 * @return <RequestType>
 * pass in a function, a suspend function, that loads data from your rest api and returns an object of <RequestType>
 *
 * @param saveFetchResult
 * @return Unit
 * pass in a function that just takes in <RequestType> (The data type got from the network) and saves it in the local cache.
 *
 * @param dbUpdateStrategy
 * @return <ResultType>
 * Takes in 2 params (remote and local) ,executes the update strategy and returns a remote-db-synchronized list.
 *
 *
 * @param networkFLowObserver
 * @return <ResultType>
 * pass in a function that observes via a callback flow the network connectivity and emits changes of network.
 *
 * @param isDbEmpty
 * @return Boolean
 * indicates whether to observe local database flow as the Single Source of Truth or not (it's already observed from viewModel)
 *
 *
 * @param
 * @return Flow<GCResult<ResultType>>
 * pass in a function that has the logic to whether the algorithm should make a networking call or not.
 * In this case, this function takes in data loaded from @param query and determines whether to make a networking call or not.
 * This can vary with your implementation however, say fetch depending on the last time you made a networking call....e.t.c.
 *
 */

inline fun <ResultType> networkBoundResource(
    crossinline isDbEmpty: () -> Boolean,
    crossinline query: suspend () -> Flow<ResultType>,
    crossinline fetch: suspend () -> ResultType,
    crossinline saveFetchResult: suspend (remote: ResultType) -> Unit,
    crossinline shouldFetch: suspend () -> Boolean = { true },
    crossinline networkFLowObserver: suspend () -> Flow<ConnectionState>,
    crossinline dbUpdateStrategy: suspend (remote: ResultType, local: ResultType) -> ResultType? = { _: ResultType, _: ResultType -> null },
) = flow {
    networkFLowObserver()
        .catch {
            emit(
                GCResult.Error(
                    data =
                    if (isDbEmpty())
                        null
                    else
                        query()
                )
            )

        }.collect { connectionState ->

            when (connectionState) {
                ConnectionState.Unavailable -> {
                    emit(
                        GCResult.ErrorNetwork(
                            data =
                            if (isDbEmpty())
                                null
                            else
                                query()
                        )
                    )
                }

                ConnectionState.Available -> {
                    if (shouldFetch()) {
                        //make a networking call
                        val remoteData = fetch()
                        //First step, fetch data from the local cache
                        val localData = query().first()
                        //emit flow of Api result as soon as it's received from api
                        val syncedData = dbUpdateStrategy(remoteData, localData)
                        //save it to the database
                        if (syncedData != null) saveFetchResult(syncedData)
                        else saveFetchResult(remoteData)
                    }
                    //Now fetch data again from the database and Dispatch it to the UI
                    emit(GCResult.Success(data = query()))

                }

                // ConnectionState.Restored -> emit(Result.NetworkRestored)
                ConnectionState.Restored -> {
                    if (shouldFetch()) {
                        //make a networking call
                        val remoteData = fetch()
                        //First step, fetch data from the local cache
                        val localData = query().first()
                        //emit flow of Api result as soon as it's received from api
                        val syncedData = dbUpdateStrategy(remoteData, localData)
                        //save it to the database
                        if (syncedData != null) saveFetchResult(syncedData)
                        else saveFetchResult(remoteData)
                    }
                    //Now fetch data again from the database and Dispatch it to the UI
                    emit(GCResult.NetworkRestored(data = query()))
                }
            }
        }
}
