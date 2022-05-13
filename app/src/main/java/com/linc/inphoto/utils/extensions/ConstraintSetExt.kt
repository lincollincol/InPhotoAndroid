package com.linc.inphoto.utils.extensions

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintLayout.updateConstraintSet(modify: ConstraintSet.() -> Unit) =
    ConstraintSet().let { set ->
        set.clone(this)
        modify(set)
        set.applyTo(this)
    }

/**
 * Vertical constraints
 */
fun ConstraintSet.connectTopToBottomOf(view: View, target: View) =
    connectTopToBottomOf(view.id, target.id)

fun ConstraintSet.connectBottomToTopOf(view: View, target: View) =
    connectBottomToTopOf(view.id, target.id)

fun ConstraintSet.connectBottomToBottomOf(view: View, target: View) =
    connectBottomToBottomOf(view.id, target.id)

fun ConstraintSet.connectTopToTopOf(view: View, target: View) =
    connectTopToTopOf(view.id, target.id)

fun ConstraintSet.connectTopToBottomOfParent(view: View) =
    connectTopToBottomOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.connectBottomToTopOfParent(view: View) =
    connectBottomToTopOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.connectBottomToBottomOfParent(view: View) =
    connectBottomToBottomOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.connectTopToTopOfParent(view: View) =
    connectTopToTopOf(view.id, ConstraintSet.PARENT_ID)

/**
 * Horizontal constraints
 */
fun ConstraintSet.connectStartToEndOf(view: View, target: View) =
    connectStartToEndOf(view.id, target.id)

fun ConstraintSet.connectEndToStartOf(view: View, target: View) =
    connectEndToStartOf(view.id, target.id)

fun ConstraintSet.connectEndToEndOf(view: View, target: View) =
    connectEndToEndOf(view.id, target.id)

fun ConstraintSet.connectStartToStartOf(view: View, target: View) =
    connectStartToStartOf(view.id, target.id)

fun ConstraintSet.connectStartToEndOfParent(view: View) =
    connectStartToEndOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.connectEndToStartOfParent(view: View) =
    connectEndToStartOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.connectEndToEndOfParent(view: View) =
    connectEndToEndOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.connectStartToStartOfParent(view: View) =
    connectStartToStartOf(view.id, ConstraintSet.PARENT_ID)

fun ConstraintSet.clearStart(view: View) = clear(view.id, ConstraintSet.START)

fun ConstraintSet.clearEnd(view: View) = clear(view.id, ConstraintSet.END)

fun ConstraintSet.clearTop(view: View) = clear(view.id, ConstraintSet.TOP)

fun ConstraintSet.clearBottom(view: View) = clear(view.id, ConstraintSet.BOTTOM)


private fun ConstraintSet.connectTopToBottomOf(view: Int, target: Int) =
    connect(view, ConstraintSet.TOP, target, ConstraintSet.BOTTOM)

private fun ConstraintSet.connectBottomToTopOf(view: Int, target: Int) =
    connect(view, ConstraintSet.BOTTOM, target, ConstraintSet.TOP)

private fun ConstraintSet.connectBottomToBottomOf(view: Int, target: Int) =
    connect(view, ConstraintSet.BOTTOM, target, ConstraintSet.BOTTOM)

private fun ConstraintSet.connectTopToTopOf(view: Int, target: Int) =
    connect(view, ConstraintSet.TOP, target, ConstraintSet.TOP)

private fun ConstraintSet.connectStartToEndOf(view: Int, target: Int) =
    connect(view, ConstraintSet.START, target, ConstraintSet.END)

private fun ConstraintSet.connectEndToStartOf(view: Int, target: Int) =
    connect(view, ConstraintSet.END, target, ConstraintSet.START)

private fun ConstraintSet.connectEndToEndOf(view: Int, target: Int) =
    connect(view, ConstraintSet.END, target, ConstraintSet.END)

private fun ConstraintSet.connectStartToStartOf(view: Int, target: Int) =
    connect(view, ConstraintSet.START, target, ConstraintSet.START)