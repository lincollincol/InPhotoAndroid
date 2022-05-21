package com.linc.inphoto.utils.extensions

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

fun getEnabledColorStateList(
    context: Context,
    @ColorRes enabledColor: Int,
    @ColorRes disabledColor: Int
) = getSelectedColorStateList(
    context.getResColor(enabledColor),
    context.getResColor(disabledColor)
)

fun getSelectedColorStateList(
    context: Context,
    @ColorRes selectedColor: Int,
    @ColorRes unselectedColor: Int
) = getSelectedColorStateList(
    context.getResColor(selectedColor),
    context.getResColor(unselectedColor)
)

fun getCheckedColorStateList(
    context: Context,
    @ColorRes checkedColor: Int,
    @ColorRes uncheckedColor: Int
) = getSelectedColorStateList(
    context.getResColor(checkedColor),
    context.getResColor(uncheckedColor)
)

fun getEnabledColorStateList(
    @ColorInt enabledColor: Int,
    @ColorInt disabledColor: Int
) = getColorStateList(
    enabledColor,
    disabledColor,
    android.R.attr.state_enabled
)

fun getSelectedColorStateList(
    @ColorInt selectedColor: Int,
    @ColorInt unselectedColor: Int
) = getColorStateList(
    selectedColor,
    unselectedColor,
    android.R.attr.state_selected
)

fun getCheckedColorStateList(
    @ColorInt checkedColor: Int,
    @ColorInt uncheckedColor: Int
) = getColorStateList(
    checkedColor,
    uncheckedColor,
    android.R.attr.state_checked
)

private fun getColorStateList(
    @ColorInt positiveStateColor: Int,
    @ColorInt negativeStateColor: Int,
    @AttrRes state: Int,
) = ColorStateList(
    arrayOf(intArrayOf(state), intArrayOf(-state)),
    intArrayOf(positiveStateColor, negativeStateColor)
)