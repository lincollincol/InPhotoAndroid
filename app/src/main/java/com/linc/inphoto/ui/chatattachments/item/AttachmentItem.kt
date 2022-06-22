package com.linc.inphoto.ui.chatattachments.item

import android.view.View
import android.widget.ImageView
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMediaAttachmentBinding
import com.linc.inphoto.ui.chatattachments.model.*
import com.linc.inphoto.utils.extensions.getMimeTypePrefix
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class AttachmentItem(
    private val attachmentUiState: AttachmentUiState
) : BindableItem<ItemMediaAttachmentBinding>(attachmentUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemMediaAttachmentBinding, position: Int) {
        val localMedia = attachmentUiState.localMedia
        with(viewBinding) {
            when {
                attachmentUiState.isVideo || attachmentUiState.isImage ->
                    previewImageView.loadImage(
                        image = localMedia.uri,
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    )
                attachmentUiState.isAudio -> previewImageView.loadImage(R.drawable.ic_music_note)
                attachmentUiState.isDoc -> previewImageView.loadImage(R.drawable.ic_document_file)
            }
            nameTextView.text = localMedia.name
            typeTextView.text = localMedia.mimeType.getMimeTypePrefix()
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemMediaAttachmentBinding>) {
        super.unbind(viewHolder)
        with(viewHolder.binding) {
            previewImageView.clearImage()
        }
    }

    override fun getLayout(): Int = R.layout.item_media_attachment

    override fun initializeViewBinding(view: View) = ItemMediaAttachmentBinding.bind(view)
}