package com.linc.inphoto.data.network.model.chat

import com.google.firebase.firestore.Exclude
import com.linc.inphoto.utils.extensions.EMPTY

data class ChatFirebaseModel(val participants: List<String>) {
    @Exclude
    @JvmField
    var id: String = String.EMPTY
}