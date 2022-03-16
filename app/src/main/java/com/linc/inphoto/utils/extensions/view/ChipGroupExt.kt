package com.linc.inphoto.utils.extensions.view

import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.addChip(
    tag: String,
    @LayoutRes layoutId: Int,
    onChipClick: (() -> Unit)? = null,
    onChipAdded: (() -> Unit)? = null,
    onChipDeleted: (() -> Unit)? = null,
) {
    val chip = LayoutInflater.from(context).inflate(layoutId, this, false) as? Chip
    chip?.apply {
        id = ViewCompat.generateViewId()
        text = tag
        setOnClickListener {
            onChipClick?.invoke()
        }
        setOnCloseIconClickListener {
            removeView(this)
            onChipDeleted?.invoke()
        }
    }
    addView(chip)
    onChipAdded?.invoke()
}