package com.linc.inphoto.utils.view.span

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class SimpleClickableSpan(
    private val underlined: Boolean = false,
    private val action: () -> Unit
) : ClickableSpan() {
    override fun onClick(widget: View) {
        action()
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = underlined
    }
}
