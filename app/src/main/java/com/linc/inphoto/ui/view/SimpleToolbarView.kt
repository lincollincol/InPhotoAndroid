package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LayoutEditorToolbarBinding
import com.linc.inphoto.utils.extensions.getColorInt
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.show

class SimpleToolbarView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private var binding: LayoutEditorToolbarBinding? = null

    private var onCancelClickListener: (() -> Unit)? = null
    private var onDoneClickListener: (() -> Unit)? = null
    private var onTitleClickListener: (() -> Unit)? = null
    private var onImageClickListener: (() -> Unit)? = null

    private var avatarImageUrl: String? = null
    private var textTitle: String?

    @ColorInt
    private var textColorTitle: Int
    private var textTitleBold: Boolean
    private var textSize: Int
    private var doneVisible: Boolean
    private var cancelVisible: Boolean
    private var avatarVisible: Boolean
    private var doneIcon: Drawable?
    private var cancelIcon: Drawable?
    private var avatarRoundPercent: Float

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.EditorToolbarView)
        textTitle = attributes.getString(R.styleable.EditorToolbarView_textTitle)
        doneIcon = attributes.getDrawable(R.styleable.EditorToolbarView_doneIcon)
        cancelIcon = attributes.getDrawable(R.styleable.EditorToolbarView_cancelIcon)
        textColorTitle = attributes.getColor(
            R.styleable.EditorToolbarView_textColorTitle,
            context.getColorInt(R.color.black)
        )
        textSize = attributes.getDimensionPixelSize(
            R.styleable.EditorToolbarView_textSize,
            resources.getDimensionPixelSize(R.dimen.font_small)
        )
        textTitleBold = attributes.getBoolean(R.styleable.EditorToolbarView_textTitleBold, true)
        doneVisible = attributes.getBoolean(R.styleable.EditorToolbarView_doneVisible, true)
        cancelVisible = attributes.getBoolean(R.styleable.EditorToolbarView_cancelVisible, true)
        avatarVisible = attributes.getBoolean(R.styleable.EditorToolbarView_avatarVisible, false)
        avatarRoundPercent = attributes.getFloat(
            R.styleable.EditorToolbarView_avatarRoundPercent,
            1f
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
            avatarImageView.apply {
                loadImage(avatarImageUrl, reloadImage = false)
                show(avatarVisible)
                roundPercent = avatarRoundPercent
                setOnClickListener {
                    onImageClickListener?.invoke()
                }
            }
            titleTextView.apply {
                text = textTitle
                setTextSize(TypedValue.COMPLEX_UNIT_PX, this@SimpleToolbarView.textSize.toFloat())
                setTextColor(textColorTitle)
                setTypeface(typeface, if (textTitleBold) Typeface.BOLD else Typeface.NORMAL)
                setOnClickListener {
                    onTitleClickListener?.invoke()
                }
            }
            doneImageView.apply {
                show(doneVisible)
                doneIcon?.let(::setImageDrawable)
                setOnClickListener {
                    onDoneClickListener?.invoke()
                }
            }
            cancelImageView.apply {
                show(cancelVisible)
                cancelIcon?.let(::setImageDrawable)
                setOnClickListener {
                    onCancelClickListener?.invoke()
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
        onCancelClickListener = null
        onDoneClickListener = null
        onTitleClickListener = null
        onImageClickListener = null
    }

    fun setOnCancelClickListener(onCancelClickListener: () -> Unit) {
        this.onCancelClickListener = onCancelClickListener
    }

    fun setOnDoneClickListener(onDoneClickListener: () -> Unit) {
        this.onDoneClickListener = onDoneClickListener
    }

    fun setOnTitleClickListener(onTitleClickListener: () -> Unit) {
        this.onTitleClickListener = onTitleClickListener
    }

    fun setOnImageClickListener(onImageClickListener: () -> Unit) {
        this.onImageClickListener = onImageClickListener
    }

    fun setToolbarTitle(title: String?) {
        this.textTitle = title
        binding?.titleTextView?.text = title
    }

    fun loadAvatarImage(imageUrl: String?) {
        this.avatarImageUrl = imageUrl
        binding?.avatarImageView?.loadImage(imageUrl, reloadImage = false)
    }

}