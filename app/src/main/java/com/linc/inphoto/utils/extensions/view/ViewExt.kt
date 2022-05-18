package com.linc.inphoto.utils.extensions.view

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.core.view.isVisible
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.view.DoubleClickListener
import com.linc.inphoto.utils.view.WordsTouchListener

@IntDef(ANIM_NONE, ANIM_FADE)
annotation class ViewAnimation

const val ANIM_NONE = 0
const val ANIM_FADE = 1

fun View.show(condition: Boolean, @ViewAnimation animation: Int = ANIM_NONE) {
    if (condition == isVisible) return
    if (condition) show(animation) else hide(animation)
}

fun View.show(@ViewAnimation animation: Int = ANIM_NONE) {
    visibility = View.VISIBLE
    if (animation != ANIM_NONE) animate().alpha(1f).start()
}

fun View.hide(@ViewAnimation animation: Int = ANIM_NONE) {
    visibility = View.GONE
    if (animation != ANIM_NONE) animate().alpha(1f).start()
}

fun View.dissolve(@ViewAnimation animation: Int = ANIM_NONE) {
    visibility = View.INVISIBLE
    if (animation != ANIM_NONE) animate().alpha(1f).start()
}

fun View.enable(condition: Boolean) {
    if (condition) enable() else disable()
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.select(condition: Boolean) {
    if (condition) select() else deselect()
}

fun View.select() {
    isSelected = true
}

fun View.deselect() {
    isSelected = false
}

fun View.toggleSelect() {
    isSelected = !isSelected
}

fun View.performPressed() {
    isPressed = true
    isPressed = false
}

fun View.setMargin(
    startMargin: Int = 0,
    topMargin: Int = 0,
    endMargin: Int = 0,
    bottomMargin: Int = 0
) {
    val layoutParams = layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
    layoutParams?.setMargins(
        startMargin,
        topMargin,
        endMargin,
        bottomMargin
    )
    this.layoutParams = layoutParams
}

fun View.changePadding(
    startPadding: Int = paddingStart,
    topPadding: Int = paddingTop,
    endPadding: Int = paddingEnd,
    bottomPadding: Int = paddingBottom
) {
    setPadding(
        startPadding,
        topPadding,
        endPadding,
        bottomPadding
    )
}

fun View.marginStart() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.leftMargin
        ?: 0

fun View.marginTop() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.topMargin
        ?: 0

fun View.marginEnd() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.rightMargin
        ?: 0

fun View.marginHorizontal() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.let { it.leftMargin + it.rightMargin }
        ?: 0

fun View.marginVertical() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.let { it.topMargin + it.bottomMargin }
        ?: 0

fun View.marginBottom() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.bottomMargin
        ?: 0

fun View.setOnThrottledClickListener(
    throttleDelay: Long = 500L,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun View.setOnDoubleClickListener(
    interval: Long = 500L,
    action: (view: View) -> Unit
) = setOnClickListener(DoubleClickListener(interval, action))

fun View.setBackgroundRipple(@ColorInt color: Int) {
    background = RippleDrawable(
        ColorStateList(arrayOf(intArrayOf()), intArrayOf(color)),
        background,
        null
    )
}

fun TextView.setWordsClickListener(
    throttleDelay: Long = 500L,
    words: List<CharSequence>,
    onTargetClicked: (CharSequence) -> Unit,
    onTextClicked: () -> Unit
) = setOnTouchListener(
    WordsTouchListener(
        throttleDelay,
        text,
        words,
        onTargetClicked,
        onTextClicked
    )
)

fun View.getBitmap(desiredWidth: Int? = null, desiredHeight: Int? = null): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        desiredWidth ?: width,
        desiredHeight ?: height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
//    invalidate()
//    return safeCast<BitmapDrawable>()?.bitmap
}