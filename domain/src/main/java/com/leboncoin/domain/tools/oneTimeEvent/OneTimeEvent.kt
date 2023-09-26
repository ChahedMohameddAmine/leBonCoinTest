package com.leboncoin.domain.tools.oneTimeEvent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Used to represent a one-shot UI event within an [MviViewState], so that we don't have to
 * toggle [Boolean] values or use timers in Rx or anything too wild. [consumeOnce] allows you to
 * process the event only once.
 *
 * Can store whatever data you might want - most of the time this would be a [String] or
 * res ID [Int].
 */
open class OneTimeEvent<T>(val payload: T? = null) {

    private val isConsumed = AtomicBoolean(false)

    internal fun getValue(): T? =
        if (isConsumed.compareAndSet(false, true)) payload
        else null

    fun isConsumed() = isConsumed.get()

    fun resetEvent() {
        isConsumed.set(false)
    }
}

fun <T> T.toOneTimeEvent() =
    OneTimeEvent(this)

/**
 * Allows you to consume the [OneTimeEvent.payload] of the event only once,
 * as it will be marked as consumed on access.
 */

fun <T> OneTimeEvent<T>?.consumeOnce(block: (T) -> Unit) {
    this?.getValue()?.let { block(it) }
}

suspend fun <T> OneTimeEvent<T>?.coConsumeOnce(
    block: suspend (T) -> Unit
) {
    this?.getValue()?.let { block(it) }
}

 fun <T> OneTimeEvent<T>?.coConsumeOnce(
    coroutineScope: CoroutineScope,
    block: suspend (T) -> Unit

) {
    this?.getValue()?.let {
        coroutineScope.launch {
            block(it)
        }
    }
}