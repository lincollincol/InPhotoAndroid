package com.linc.inphoto.ui.managepost.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ManagePostIntent : Parcelable {
    @Parcelize
    data class NewPost(val imageUri: Uri) : ManagePostIntent()
    @Parcelize
    data class EditPost(
        val postId: String,
        val contentUrl: String,
        val description: String,
        val tags: List<String>
    ) : ManagePostIntent()
}