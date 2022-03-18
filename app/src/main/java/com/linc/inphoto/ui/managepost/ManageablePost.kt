package com.linc.inphoto.ui.managepost

import android.net.Uri
import android.os.Parcelable
import com.linc.inphoto.utils.extensions.EMPTY
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ManageablePost(
    val imageUri: Uri,
    val description: String = String.EMPTY,
    val tags: List<String> = emptyList()
) : Parcelable