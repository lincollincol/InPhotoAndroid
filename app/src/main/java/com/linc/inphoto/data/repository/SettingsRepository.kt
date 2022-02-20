package com.linc.inphoto.data.repository

import com.linc.inphoto.data.android.RatioLocalDataSource
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val ratioLocalDataSource: RatioLocalDataSource
) {

    fun loadAspectRatios() = ratioLocalDataSource.loadAspectRatios()

}