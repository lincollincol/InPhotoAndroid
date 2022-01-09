package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.linc.inphoto.R
import com.linc.inphoto.databinding.LoadingOverlayLayoutBinding
import com.linc.inphoto.utils.extensions.getActivity
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.safeCast
import eightbitlab.com.blurview.RenderScriptBlur

class LoadingView constructor(
    context: Context,
    attributeSet: AttributeSet,
) : FrameLayout(context, attributeSet) {

    companion object {
        private const val DEFAULT_RADIUS = 1f

        @ColorInt
        private const val DEFAULT_COLOR = Color.TRANSPARENT
        private const val DEFAULT_ENABLE_STATE = true
    }

    private var binding: LoadingOverlayLayoutBinding? = null
    private var blurRadius: Float

    @ColorInt
    private var blurColor: Int
    private var blurEnabled: Boolean
    private var alreadyBlurred: Boolean = false

    init {
        val attributes = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.LoadingView
        )

        blurRadius = attributes.getFloat(R.styleable.LoadingView_blurRadius, DEFAULT_RADIUS)
        blurColor = attributes.getColor(R.styleable.LoadingView_blurOverlayColor, DEFAULT_COLOR)
        blurEnabled = attributes.getBoolean(R.styleable.LoadingView_enable, DEFAULT_ENABLE_STATE)

        attributes.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }

        val windowBackground = context.getActivity()?.window?.decorView?.background
        val rootView = rootView.findViewById<View>(android.R.id.content)?.safeCast<ViewGroup>()



        binding = LoadingOverlayLayoutBinding.inflate(context.inflater, this, true)
        binding?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                rootView?.setRenderEffect(
                    RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
                )
            } else {
                if (rootView == null || alreadyBlurred) {
                    return
                }
                blurLayout.setupWith(rootView)
                    .setFrameClearDrawable(windowBackground)
                    .setBlurAlgorithm(RenderScriptBlur(context))
                    .setBlurRadius(blurRadius)
                    .setOverlayColor(blurColor)
                    .setBlurEnabled(blurEnabled)
                    .setBlurAutoUpdate(true)
                    .setHasFixedTransformationMatrix(true)
                alreadyBlurred = true
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

}