package com.linc.inphoto.utils.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class InPhotoGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        builder.setLogLevel(Log.ERROR)
        super.applyOptions(context, builder)
    }
}