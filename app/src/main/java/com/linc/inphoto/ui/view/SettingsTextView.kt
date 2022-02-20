package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutSettingsTextViewBinding
import com.linc.inphoto.utils.extensions.getColorInt
import com.linc.inphoto.utils.extensions.inflater

class SettingsTextView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private var binding: LayoutSettingsTextViewBinding? = null

    private var textTitle: String?
    private var textValue: String?
    @ColorInt
    private var textColorTitle: Int
    @ColorInt
    private var textColorValue: Int
    private var textTitleBold: Boolean
    private var textValueBold: Boolean
    private var textSize: Int

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SettingsTextView)

        textTitle = attributes.getString(R.styleable.SettingsTextView_textTitle)
        textValue = attributes.getString(R.styleable.SettingsTextView_textValue)
        textColorTitle = attributes.getColor(
            R.styleable.SettingsTextView_textColorTitle,
            context.getColorInt(R.color.black)
        )
        textColorValue = attributes.getColor(
            R.styleable.SettingsTextView_textColorValue,
            context.getColorInt(R.color.black_50)
        )
        textTitleBold = attributes.getBoolean(
            R.styleable.SettingsTextView_textTitleBold,
            true
        )
        textValueBold = attributes.getBoolean(
            R.styleable.SettingsTextView_textValueBold,
            false
        )
        textSize = attributes.getDimensionPixelSize(
            R.styleable.SettingsTextView_textSize,
            resources.getDimensionPixelSize(R.dimen.font_small)
        )
        attributes.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }

        binding = LayoutSettingsTextViewBinding.inflate(context.inflater, this, true)
        binding?.run {
            applyTextViewAttrs(titleTextView, textTitle, textSize, textColorTitle, textTitleBold)
            applyTextViewAttrs(valueTextView, textValue, textSize, textColorValue, textValueBold)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

    private fun applyTextViewAttrs(
        textView: TextView,
        content: String?,
        textSize: Int,
        @ColorInt textColor: Int,
        bold: Boolean
    ) = textView.apply {
        text = content
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//        this.textSize = textSize
        setTextColor(textColor)
        setTypeface(typeface, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

}