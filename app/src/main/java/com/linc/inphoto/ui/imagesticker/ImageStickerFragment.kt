package com.linc.inphoto.ui.imagesticker

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentImageStickerBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.imagesticker.item.ImageStickerItem
import com.linc.inphoto.ui.imagesticker.model.ImageStickerIntent
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.horizontalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator

@AndroidEntryPoint
class ImageStickerFragment : BaseFragment(R.layout.fragment_image_sticker) {

    companion object {
        private const val INTENT_ARG = "intent_key"
        private const val IMAGE_URI_ARG = "image_uri"

        @JvmStatic
        fun newInstance(
            intent: ImageStickerIntent,
            image: Uri
        ) = ImageStickerFragment().apply {
            arguments = bundleOf(
                INTENT_ARG to intent,
                IMAGE_URI_ARG to image
            )
        }
    }

    override val viewModel: ImageStickerViewModel by viewModels()
    private val binding by viewBinding(FragmentImageStickerBinding::bind)
    private val stickersSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            stickersSection.update(state.availableStickers.map(::ImageStickerItem))
            state.selectedStickers.firstOrNull()?.let {
                overlayImageView.addSticker(it.uri)
                viewModel.stickerAdded(it)
            }
            overlayImageView.setImageUri(state.image)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadStickers(getArgument(IMAGE_URI_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            overlayImageView.registerLifecycleOwner(lifecycle)
            stickersRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = createAdapter(stickersSection)
                itemAnimator = FadeInRightAnimator()
            }
            toolbarView.apply {
                setOnCancelClickListener {
                    viewModel.onBackPressed()
                }
                setOnDoneClickListener {
                    // TODO: 17.05.22 get and save image
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.overlayImageView.unregisterLifecycleOwner(lifecycle)
        super.onDestroyView()
    }
}