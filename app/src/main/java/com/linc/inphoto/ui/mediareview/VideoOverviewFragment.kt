package com.linc.inphoto.ui.mediareview

import android.net.Uri
import android.os.Build
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentVideoOverviewBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.base.viewmodel.EmptyViewModel
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoOverviewFragment : BaseFragment(R.layout.fragment_video_overview) {

    companion object {
        private const val VIDEO_ARG = "video"

        @JvmStatic
        fun newInstance(videoUri: Uri) = VideoOverviewFragment().apply {
            arguments = bundleOf(VIDEO_ARG to videoUri)
        }
    }

    override val viewModel: EmptyViewModel by viewModels()
    private val binding by viewBinding(FragmentVideoOverviewBinding::bind)
    private var player: ExoPlayer? = null

    override suspend fun observeUiState() {
        // Empty state
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= Build.VERSION_CODES.N) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(requireContext())
            .setHandleAudioBecomingNoisy(true)
            .build()
        player?.run {
            binding.videoPlayerView.player = this
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            setMediaItem(MediaItem.fromUri(getArgumentNotNull<Uri>(VIDEO_ARG)))
            prepare()
        }
    }

    private fun releasePlayer() {
        player?.run {
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

}