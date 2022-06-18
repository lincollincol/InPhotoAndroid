package com.linc.inphoto.ui.audiolibrary.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemLibraryAudioBinding
import com.linc.inphoto.ui.audiolibrary.model.AudioUiState
import com.xwray.groupie.viewbinding.BindableItem

class AudioItem(
    private val audioUiState: AudioUiState
) : BindableItem<ItemLibraryAudioBinding>() {
    override fun bind(viewBinding: ItemLibraryAudioBinding, position: Int) {
        viewBinding.audioTextView.text = audioUiState.name
    }

    override fun getLayout(): Int = R.layout.item_library_audio

    override fun initializeViewBinding(view: View) = ItemLibraryAudioBinding.bind(view)
}