package com.linc.inphoto.utils.view.progress

import com.genius.multiprogressbar.MultiProgressBar

class MultiProgressStepListener(
    private val onStepChanged: (step: Int) -> Unit
) : MultiProgressBar.ProgressStepChangeListener {
    override fun onProgressStepChange(newStep: Int) {
        onStepChanged(newStep)
    }
}