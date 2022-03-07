package com.linc.inphoto.ui.editimage.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemEditOperationBinding
import com.linc.inphoto.ui.editimage.model.EditOperation
import com.xwray.groupie.viewbinding.BindableItem

class EditOperationItem(
    private val editOperation: EditOperation
) : BindableItem<ItemEditOperationBinding>(editOperation.title.hashCode().toLong()) {

    override fun bind(viewBinding: ItemEditOperationBinding, position: Int) {

    }

    override fun getLayout(): Int = R.layout.item_edit_operation

    override fun initializeViewBinding(view: View) = ItemEditOperationBinding.bind(view)
}