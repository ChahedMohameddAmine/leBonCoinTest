package com.leboncoin.albumsample.presentation.main.album

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.leboncoin.albumsample.R
import com.leboncoin.albumsample.databinding.FragmentAlbumBinding
import com.leboncoin.albumsample.presentation.base.BaseFragment
import com.leboncoin.albumsample.presentation.main.album.adapter.AlbumAdapter
import com.leboncoin.albumsample.presentation.main.album.state_event.AlbumEvent
import com.leboncoin.albumsample.presentation.main.album.state_event.AlbumUiState
import com.leboncoin.albumsample.presentation.tools.bottomview.DurationBottomView
import com.leboncoin.albumsample.presentation.tools.bottomview.SnackBarAlert
import com.leboncoin.albumsample.presentation.tools.bottomview.ToastAlert
import com.leboncoin.albumsample.presentation.tools.showBriefly
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState.Companion.IS_ERROR_NETWORK
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState.Companion.IS_ERROR_SERVER
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState.Companion.IS_NETWORK_RESTORED
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState.Companion.IS_NO_DATA_ERROR_NETWORK
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState.Companion.IS_NO_DATA_ERROR_SERVER
import com.leboncoin.domain.tools.oneTimeEvent.coConsumeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : BaseFragment<FragmentAlbumBinding>() {

    override fun getViewBinding() = FragmentAlbumBinding.inflate(layoutInflater)

    private val viewModel: AlbumViewModel by viewModels()


    override val bottomViewAlert by lazy { ToastAlert() }

    private val albumAdapter: AlbumAdapter by lazy {
        AlbumAdapter(
            onAlbumClicked = { album ->
                bottomViewAlert.makeBottomView(
                    view = requireView(),
                    msg = "Clicked : ${album.title}",
                    durationBottomView = DurationBottomView.SHORT
                ).show()
            },
            onAlbumFavoriteClicked = { album ->
                viewModel.setAsFavorite(album)
            })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.albumRecycler.adapter = albumAdapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { albumUi ->
                    binding.swipeToRefresh.isRefreshing = albumUi.isLoading
                    onConnectivityChanged(albumUi)
                    albumAdapter.submitList(albumUi.data)
                }
            }
        }
        //Get Albums from repository (remote DataSource + Local)
        viewModel.state.handleEvent(AlbumEvent.ScreenStarted)

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.state.handleEvent(AlbumEvent.OnRefreshAlbum)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.state.handleEvent(AlbumEvent.ScreenStarted)
    }

    private fun onConnectivityChanged(albumUi: AlbumUiState) {
        albumUi.networkState?.coConsumeOnce(coroutineScope = lifecycleScope) { netWorkState ->
            when (netWorkState) {
                IS_ERROR_NETWORK -> {
                    setSnackBarStyle(
                        color = R.color.carbon_black,
                        stringRes = R.string.api_indicator_offline
                    )
                    binding.networkBottomIndicator.isVisible = true
                }

                IS_NETWORK_RESTORED -> {
                    setSnackBarStyle(
                        color = R.color.green,
                        stringRes = R.string.api_indicator_online
                    )
                    binding.connectivityErrorLayout.root.isVisible = false
                    binding.networkBottomIndicator.showBriefly()
                }

                IS_ERROR_SERVER -> {
                    bottomViewAlert.makeBottomView(
                        view = requireView(),
                        msg = "Server Error",
                        durationBottomView = DurationBottomView.SHORT
                    ).show()
                }

                IS_NO_DATA_ERROR_SERVER -> {
                    binding.serverErrorLayout.root.isVisible = albumUi.data.isEmpty()
                }

                IS_NO_DATA_ERROR_NETWORK -> {
                    binding.connectivityErrorLayout.root.isVisible = true
                }
            }
        }


    }

    private fun setSnackBarStyle(@ColorRes color: Int, @StringRes stringRes: Int) {
        binding.networkBottomIndicator.setBackgroundColor(
            resources.getColor(color)
        )
        binding.networkBottomIndicator.text =
            binding.root.context.getString(stringRes)
    }


    private fun LifecycleCoroutineScope.launchUniquely(
        block: suspend CoroutineScope.() -> Unit
    ) {
        this.cancel()
        this.launch {

        }
    }
}