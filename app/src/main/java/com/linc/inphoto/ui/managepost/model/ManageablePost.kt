package com.linc.inphoto.ui.managepost.model

import android.net.Uri
import android.os.Parcelable
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.utils.extensions.EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ManageablePost(
    val imageUri: Uri,
    val description: String = String.EMPTY,
    val tags: List<String> = emptyList(),
    val id: String? = null
) : Parcelable {
    constructor(post: ExtendedPost) : this(
        Uri.parse(post.contentUrl),
        post.description,
        post.tags,
        post.id
    )
}