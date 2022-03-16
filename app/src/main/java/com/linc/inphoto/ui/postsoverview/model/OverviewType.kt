package com.linc.inphoto.ui.postsoverview.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class OverviewType(open val postId: String) : Parcelable {

    @Parcelize
    class Profile(override val postId: String, val userId: String) : OverviewType(postId)

    @Parcelize
    class Feed(override val postId: String) : OverviewType(postId)

}