package com.linc.inphoto.ui.mediareview

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentImageOverviewBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.base.viewmodel.EmptyViewModel
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.view.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageOverviewFragment : BaseFragment(R.layout.fragment_image_overview) {

    companion object {
        private const val IMAGE_ARG = "image"

        @JvmStatic
        fun newInstance(imageUri: Uri) = ImageOverviewFragment().apply {
            arguments = bundleOf(IMAGE_ARG to imageUri)
        }
    }

    override val viewModel: EmptyViewModel by viewModels()
    private val binding by viewBinding(FragmentImageOverviewBinding::bind)

    override suspend fun observeUiState() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fileImageView.loadImage(getArgumentNotNull(IMAGE_ARG))
    }

}