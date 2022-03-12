package com.linc.inphoto.data.android

import com.linc.inphoto.entity.media.image.AspectRatio
import javax.inject.Inject


class RatioLocalDataSource @Inject constructor() {

    fun loadAspectRatios() = listOf(
        AspectRatio(3f, 2f),
        AspectRatio(4f, 3f),
        AspectRatio(5f, 4f),
        AspectRatio(1f, 1f),
        AspectRatio(4f, 5f),
        AspectRatio(3f, 4f),
        AspectRatio(2f, 3f)
    )
}