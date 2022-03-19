package com.linc.inphoto.ui.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutTagsEditTextBinding
import com.linc.inphoto.utils.TextInputFilter
import com.linc.inphoto.utils.extensions.EMPTY
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.view.addChip
import com.linc.inphoto.utils.extensions.view.textToString


class TagsEditText constructor(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    companion object {
        private const val TAG_LENGTH = 12
    }

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
            tagsEditText.apply {
                inputType =
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                doOnTextChanged { tag, _, _, _ ->
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
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        return@setOnFocusChangeListener
                    }
                    val tag = textToString()
                    if (tag.isEmpty() || tag.isBlank()) {
                        tagsEditText.text?.clear()
                        return@setOnFocusChangeListener
                    }

                    val tagAdded = addChip(tag)
                    if (tagAdded) {
                        tagsEditText.text?.clear()
                    } else {
                        tagsEditText.text?.delete(tag.length - 1, tag.length)
                    }
                }
                filters = arrayOf(TextInputFilter { text, start, end, spanned, _, _ ->
                    if (spanned?.length ?: 0 >= TAG_LENGTH - 1 && text?.isBlank() == false) {
                        return@TextInputFilter String.EMPTY
                    }
                    return@TextInputFilter text?.subSequence(start, end)
                        ?.replace(Regex("([^A-Za-z0-9\\p{L}_ ])"), String.EMPTY)
                })
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

}