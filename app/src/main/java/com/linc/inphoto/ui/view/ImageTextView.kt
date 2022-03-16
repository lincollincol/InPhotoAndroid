package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.view.updateLayoutParams
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutImageTextViewBinding
import com.linc.inphoto.utils.extensions.getColorInt
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.view.setTint


class ImageTextView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private var binding: LayoutImageTextViewBinding? = null
    private var onIconClickListener: (() -> Unit)? = null

    @ColorInt
    private var textColor: Int

    @ColorInt
    private var iconTint: Int
    private var iconSize: Int
    private var textSize: Int
    private var text: String?
    private var icon: Drawable?

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTextView)
        icon = attributes.getDrawable(R.styleable.ImageTextView_icon)
        text = attributes.getString(R.styleable.ImageTextView_text)
        textColor = attributes.getColor(
            R.styleable.ImageTextView_textColor,
            context.getColorInt(R.color.black)
        )
        iconTint = attributes.getColor(
            R.styleable.ImageTextView_iconTint,
            context.getColorInt(R.color.black)
        )
        textSize = attributes.getDimensionPixelSize(
            R.styleable.ImageTextView_textSize,
            resources.getDimensionPixelSize(R.dimen.font_small)
        )
        iconSize = attributes.getDimensionPixelSize(
            R.styleable.ImageTextView_iconSize,
            resources.getDimensionPixelSize(R.dimen.size_icon_small)
        )
        attributes.recycle()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }

        binding = LayoutImageTextViewBinding.inflate(context.inflater, this, true)
        binding?.run {
            textView.apply {
                text = this@ImageTextView.text
                setTextColor(this@ImageTextView.textColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, this@ImageTextView.textSize.toFloat())
            }
            imageView.apply {
                setImageDrawable(icon)
                setTint(iconTint)
                updateLayoutParams {
                    width = iconSize
                    height = iconSize
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
        onIconClickListener = null
    }

    fun setOnIconClickListener(onIconClickListener: () -> Unit) {
        this.onIconClickListener = onIconClickListener
    }

    fun setText(text: String?) {
        this.text = text
        binding?.textView?.text = text
    }

    fun setText(@StringRes text: Int) {
        setText(context.getString(text))
    }

}