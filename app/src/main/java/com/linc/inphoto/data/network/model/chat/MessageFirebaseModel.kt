package com.linc.inphoto.data.network.model.chat

import com.google.firebase.firestore.Exclude
import com.linc.inphoto.data.network.model.media.RemoteMediaApiModel
import com.linc.inphoto.utils.extensions.EMPTY

data class MessageFirebaseModel(
    val userId: String,
    val text: String,
    val attachments: List<RemoteMediaApiModel>,
    val createdTimestamp: Long,
    val isEdited: Boolean
) {
    companion object {
        @JvmStatic
        fun getChatMessageInstance(
            userId: String,
            text: String,
            attachments: List<RemoteMediaApiModel>
        ) = MessageFirebaseModel(
            userId,
            text,
            attachments,
            System.currentTimeMillis(),
            isEdited = false
        )

        @JvmStatic
        fun getSystemMessageInstance() = MessageFirebaseModel(
            String.EMPTY,
            String.EMPTY,
            listOf(),
            System.currentTimeMillis(),
            isEdited = false
        )
    }

    @Exclude
    @JvmField
    var id: String = String.EMPTY

}