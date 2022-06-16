package com.linc.inphoto.utils.extensions.view

import android.graphics.Color
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.noowenz.showmoreless.ShowMoreLess

fun TextView.setExpandable(
    lines: Int,
    @StringRes showMore: Int,
    @StringRes showLess: Int,
    @ColorInt color: Int = Color.BLACK,
    expanded: Boolean = false,
    underline: Boolean = false,
    bold: Boolean = true,
    animation: Boolean = false,
    linkify: Boolean = false,
) = ShowMoreLess.Builder(context)
    .textLengthAndLengthType(
        length = lines,
        textLengthType = ShowMoreLess.TYPE_LINE
    )
    .showMoreLabel(context.getString(showMore))
    .showLessLabel(context.getString(showLess))
    .showMoreLabelColor(color)
    .showLessLabelColor(color)
    .labelUnderLine(labelUnderLine = underline)
    .labelBold(labelBold = bold)
    .expandAnimation(expandAnimation = animation)
    .enableLinkify(linkify = linkify)
    .textClickable(
        textClickableInExpand = false,
        textClickableInCollapse = false
    )
    .build()
    .addShowMoreLess(textView = this, text = text, isContentExpanded = expanded)