package com.linc.inphoto.utils.view

import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.linc.inphoto.utils.extensions.containsOneOf
import com.linc.inphoto.utils.extensions.isOneOf
import com.linc.inphoto.utils.extensions.safeCast

class StringTargetsTouchListener(
    private val text: CharSequence,
    private val targets: List<CharSequence>,
    private val onTargetClicked: (CharSequence) -> Unit,
    private val onTextClicked: () -> Unit
) : View.OnTouchListener {
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val offset = view.safeCast<TextView>()?.getOffsetForPosition(event.x, event.y) ?: 0
            val clickedText = text.extractWord(offset)
            if (clickedText.containsOneOf(*targets.toTypedArray())) {
                onTargetClicked(clickedText)
            } else {
                onTextClicked()
            }
        }
        return true
    }

    private fun CharSequence.extractWord(offset: Int): String {
        val delimiters = charArrayOf(' ', '\n', '\t')
        var startIndex = offset
        var endIndex = offset
        while (text.getOrNull(startIndex)
                ?.isOneOf(*delimiters) == false && startIndex > 0
        ) startIndex--
        while (text.getOrNull(endIndex)
                ?.isOneOf(*delimiters) == false && endIndex < length
        ) endIndex++
        return substring(startIndex, endIndex).trim()
    }
}