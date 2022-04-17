package com.linc.inphoto.ui.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.widget.doOnTextChanged
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutTitledEditTextBinding
import com.linc.inphoto.utils.extensions.inflater

@Deprecated("Use TextInputLayout instead")
class TitledEditText constructor(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {

    private var binding: LayoutTitledEditTextBinding? = null
    private val title: String?
    private var text: String?
    private val inputType: Int
    private var onTextChangedListener: ((String) -> Unit)? = null

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
            inputEditText.apply {
//                text = text
                inputType = this@TitledEditText.inputType
                doOnTextChanged { text, _, _, _ ->
                    onTextChangedListener?.invoke(text.toString())
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

    fun setOnTextChangedListener(onTextChangedListener: (String) -> Unit) {
        this.onTextChangedListener = onTextChangedListener
    }

    fun setText(text: CharSequence?) {
        this.text = text.toString()
        binding?.inputEditText?.setText(text)
    }

}