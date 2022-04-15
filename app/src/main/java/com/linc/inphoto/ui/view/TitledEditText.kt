package com.linc.inphoto.ui.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutTitledEditTextBinding
import com.linc.inphoto.utils.extensions.inflater


class TitledEditText constructor(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var binding: LayoutTitledEditTextBinding? = null
    private val title: String?
    private var text: String?
    private val inputType: Int
    private var onTagDeletedListener: ((String) -> Unit)? = null

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.TitledEditText)
        title = attributes.getString(R.styleable.TitledEditText_title)
        text = attributes.getString(R.styleable.TitledEditText_text)
        inputType = attributes.getInt(R.styleable.TitledEditText_inputType, InputType.TYPE_TEXT_VARIATION_NORMAL)
        attributes.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }
        binding = LayoutTitledEditTextBinding.inflate(context.inflater, this, true)
        binding?.run {
            titleTextView.setText(title)
            inputEditText.setText(text)
            inputEditText.inputType = inputType
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

    fun doOnTextChanged(
        action: (
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) -> Unit
    ) = binding?.inputEditText?.doOnTextChanged(action)

    fun setText(text: CharSequence?) {
        this.text = text.toString()
        binding?.inputEditText?.setText(text)
    }

}