package com.linc.inphoto.ui.audiolibrary.item

import android.view.View
import androidx.core.view.children
import androidx.transition.Fade
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemLibraryAudioBinding
import com.linc.inphoto.ui.audiolibrary.model.AudioUiState
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem

class AudioItem(
    private val audioUiState: AudioUiState
) : BindableItem<ItemLibraryAudioBinding>(audioUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemLibraryAudioBinding, position: Int) {
        with(viewBinding) {
            animateTargets(Fade(), root, root.children)
            nameTextView.text = audioUiState.localMedia.name
            selectedImageView.show(audioUiState.isSelected)
            root.setOnThrottledClickListener {
                selectedImageView.show(!audioUiState.isSelected)
                audioUiState.onClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_library_audio

    override fun initializeViewBinding(view: View) = ItemLibraryAudioBinding.bind(view)
}