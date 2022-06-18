package com.linc.inphoto.ui.audiolibrary.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemLibraryAudioBinding
import com.linc.inphoto.ui.audiolibrary.model.AudioUiState
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class AudioItem(
    private val audioUiState: AudioUiState
) : BindableItem<ItemLibraryAudioBinding>(audioUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemLibraryAudioBinding, position: Int) {
        with(viewBinding) {
            nameTextView.text = audioUiState.name
            root.setOnThrottledClickListener {
                audioUiState.onClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_library_audio

    override fun initializeViewBinding(view: View) = ItemLibraryAudioBinding.bind(view)
}