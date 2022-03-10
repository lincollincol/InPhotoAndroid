package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutEditorToolbarBinding
import com.linc.inphoto.utils.extensions.getColorInt
import com.linc.inphoto.utils.extensions.inflater

class EditorToolbarView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private var binding: LayoutEditorToolbarBinding? = null

    private var onCancelClickListener: (() -> Unit)? = null
    private var onDoneClickListener: (() -> Unit)? = null

    private var textTitle: String?

    @ColorInt
    private var textColorTitle: Int
    private var textTitleBold: Boolean
    private var textSize: Int

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.EditorToolbarView)
        textTitle = attributes.getString(R.styleable.EditorToolbarView_textTitle)
        textColorTitle = attributes.getColor(
            R.styleable.EditorToolbarView_textColorTitle,
            context.getColorInt(R.color.black)
        )
        textTitleBold = attributes.getBoolean(
            R.styleable.EditorToolbarView_textTitleBold,
            true
        )
        textSize = attributes.getDimensionPixelSize(
            R.styleable.EditorToolbarView_textSize,
            resources.getDimensionPixelSize(R.dimen.font_small)
        )
        attributes.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }

        binding = LayoutEditorToolbarBinding.inflate(context.inflater, this, true)
        binding?.run {
            titleTextView.apply {
                text = textTitle
                setTextSize(TypedValue.COMPLEX_UNIT_PX, this@EditorToolbarView.textSize.toFloat())
                setTextColor(textColorTitle)
                setTypeface(typeface, if (textTitleBold) Typeface.BOLD else Typeface.NORMAL)
            }
            doneImageView.setOnClickListener {
                onDoneClickListener?.invoke()
            }
            cancelImageView.setOnClickListener {
                onCancelClickListener?.invoke()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
        onCancelClickListener = null
        onDoneClickListener = null
    }

    fun setOnCancelClickListener(onCancelClickListener: () -> Unit) {
        this.onCancelClickListener = onCancelClickListener
    }

    fun setOnDoneClickListener(onDoneClickListener: () -> Unit) {
        this.onDoneClickListener = onDoneClickListener
    }

}