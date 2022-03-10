package com.linc.inphoto.ui.editimage.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemEditOperationBinding
import com.linc.inphoto.ui.editimage.EditorOperationUiState
import com.xwray.groupie.viewbinding.BindableItem

class EditOperationItem(
    private val editorOperationUiState: EditorOperationUiState
) : BindableItem<ItemEditOperationBinding>(editorOperationUiState.operation.hashCode().toLong()) {

    override fun bind(viewBinding: ItemEditOperationBinding, position: Int) {
        with(viewBinding) {
            iconImageView.setImageResource(editorOperationUiState.operation.icon)
            actionTextView.setText(editorOperationUiState.operation.title)
            root.setOnClickListener {
                editorOperationUiState.onClick?.invoke()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_edit_operation

    override fun initializeViewBinding(view: View) = ItemEditOperationBinding.bind(view)
}