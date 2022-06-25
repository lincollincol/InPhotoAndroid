package com.linc.inphoto.data.network.model.chat

import com.google.firebase.firestore.Exclude
import com.linc.inphoto.utils.extensions.EMPTY

data class AttachmentFirebaseModel(
    val url: String,
    val type: String
) {
    @Exclude
    @JvmField
    var id: String = String.EMPTY
}