package com.linc.inphoto.ui.filemanager.item

import android.view.View
import android.widget.ImageView
import androidx.core.view.children
import androidx.transition.Fade
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMediaFileBinding
import com.linc.inphoto.ui.filemanager.model.*
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.getMimeTypePrefix
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class FileItem(
    private val fileUiState: FileUiState
) : BindableItem<ItemMediaFileBinding>(fileUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemMediaFileBinding, position: Int) {
        val localMedia = fileUiState.localMedia
        with(viewBinding) {
            animateTargets(Fade(), root, root.children)
            when {
                fileUiState.isVideo || fileUiState.isImage ->
                    previewImageView.loadImage(
                        image = localMedia.uri,
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    )
                fileUiState.isAudio -> previewImageView.loadImage(R.drawable.ic_music_note)
                fileUiState.isDoc -> previewImageView.loadImage(R.drawable.ic_document_file)
            }
            nameTextView.text = fileUiState.localMedia.name
            typeTextView.text = localMedia.mimeType.getMimeTypePrefix()
            selectedImageView.show(fileUiState.isSelected)
            root.setOnThrottledClickListener {
                fileUiState.onClick()
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemMediaFileBinding>) {
        super.unbind(viewHolder)
        with(viewHolder.binding) {
            previewImageView.clearImage()
        }
    }

    override fun getLayout(): Int = R.layout.item_media_file

    override fun initializeViewBinding(view: View) = ItemMediaFileBinding.bind(view)
}