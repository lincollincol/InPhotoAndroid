package com.linc.inphoto.ui.postsoverview.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class OverviewType : Parcelable {

    abstract val postId: String

    @Parcelize
    class Profile(
        override val postId: String,
        val userId: String
    ) : OverviewType()

    @Parcelize
    class Feed(override val postId: String) : OverviewType()

}