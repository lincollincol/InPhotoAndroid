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
import com.linc.inphoto.utils.extensions.isLongerThan
import com.linc.inphoto.utils.extensions.view.addChip
import com.linc.inphoto.utils.extensions.view.deleteLast
import com.linc.inphoto.utils.extensions.view.textToString


class TagsEditText constructor(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    companion object {
        private const val TAG_LENGTH = 12
    }

    private var binding: LayoutTagsEditTextBinding? = null
    private val tags = hashMapOf<String, Boolean>()
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
                inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                doOnTextChanged { tag, _, _, _ ->
                    if (tag?.lastOrNull()?.isWhitespace() == true) {
                        addChip(tag.trim().toString())
                    }
                }
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus && !text.isNullOrEmpty()) {
                        addChip(textToString().trim())
                        return@setOnFocusChangeListener
                    }
                }
                filters = arrayOf(TextInputFilter { text, start, end, spanned, _, _ ->
                    if (spanned.isLongerThan(TAG_LENGTH - 1) && text?.isBlank() == false) {
                        return@TextInputFilter String.EMPTY
                    }
                    return@TextInputFilter text?.subSequence(start, end)
                        ?.replace(Regex("([^A-Za-z0-9\\p{L}_ ])"), String.EMPTY)
                })
            }
            tagsChipGroup.apply {
                tags.keys.forEach(::addChip)
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

    fun setTags(tags: Set<String>) {
        setTags(tags.toList())
    }

    fun setTags(tags: List<String>) {
        tags.forEach { tag ->
            this.tags[tag] = this.tags.containsKey(tag)
            addChip(tag)
        }
    }

    private fun addChip(tag: String): Boolean = binding?.run {
        if (tag.isEmpty() || tag.isBlank()) {
            tagsEditText.text?.clear()
            return@run false
        } else if (tags[tag] == true) {
            tagsEditText.text.deleteLast()
            return@run false
        }
        TransitionManager.beginDelayedTransition(root, AutoTransition())
        tagsChipGroup.addChip(
            tag,
            R.layout.item_editable_tag_chip,
            onChipAdded = {
                tags[tag] = true
                tagsEditText.text?.clear()
                onTagAddedListener?.invoke(tag)
            },
            onChipDeleted = {
                tags.remove(tag)
                onTagDeletedListener?.invoke(tag)
            }
        )
        return@run true
    } ?: false

}