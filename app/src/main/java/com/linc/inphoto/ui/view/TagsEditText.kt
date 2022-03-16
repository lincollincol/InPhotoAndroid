package com.linc.inphoto.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutTagsEditTextBinding
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.view.addChip


class TagsEditText constructor(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var binding: LayoutTagsEditTextBinding? = null
    private val tags = mutableListOf<String>()
    private var onTagAddedListener: ((String) -> Unit)? = null
    private var onTagDeletedListener: ((String) -> Unit)? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }

        binding = LayoutTagsEditTextBinding.inflate(context.inflater, this, true)
        binding?.run {
            tagsEditText.doOnTextChanged { tag, _, _, _ ->
                if (tag.isNullOrEmpty() || tag.isBlank()) {
                    tagsEditText.text?.clear()
                    return@doOnTextChanged
                }

                if (tag.last().isWhitespace()) {
                    val tagAdded = addChip(tag.toString())
                    if (tagAdded) {
                        tagsEditText.text?.clear()
                    } else {
                        tagsEditText.text?.delete(tag.length - 1, tag.length)
                    }
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
        onTagAddedListener = null
    }

    fun setOnTagAddedListener(onTagAddedListener: (String) -> Unit) {
        this.onTagAddedListener = onTagAddedListener
    }

    fun setOnTagDeletedListener(onTagDeletedListener: (String) -> Unit) {
        this.onTagDeletedListener = onTagDeletedListener
    }

    fun setTags(tags: List<String>) {
        if (this.tags == tags) {
            return
        }
        this.tags.clear()
        this.tags.addAll(tags)
    }

    private fun addChip(tag: String): Boolean {
        if (binding?.root == null || tags.contains(tag)) {
            return false
        }
        TransitionManager.beginDelayedTransition(binding!!.root, AutoTransition())
        binding?.tagsChipGroup?.addChip(
            tag,
            R.layout.item_editable_tag_chip,
            onChipAdded = {
                onTagAddedListener?.invoke(tag)
                tags.add(tag)
            },
            onChipDeleted = {
                onTagDeletedListener?.invoke(tag)
                tags.remove(tag)
            }
        )
        return true
    }
    /*private fun addChip(tag: String): Boolean {
        if (binding?.root == null || tags.contains(tag)) {
            return false
        }
        TransitionManager.beginDelayedTransition(binding!!.root, AutoTransition())
        val chip = LayoutInflater.from(context).inflate(
            R.layout.item_editable_tag_chip,
            binding?.tagsChipGroup,
            false
        ) as? Chip
        chip?.apply {
            id = ViewCompat.generateViewId()
            text = tag
            setOnCloseIconClickListener {
                binding?.tagsChipGroup?.removeView(this)
                onTagDeletedListener?.invoke(tag)
                tags.remove(tag)
            }
        }
        binding?.tagsChipGroup?.addView(chip)
        onTagAddedListener?.invoke(tag)
        tags.add(tag)
        return true
    }*/

}