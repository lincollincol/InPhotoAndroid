package com.linc.inphoto.data.network.model.chat

import com.google.firebase.firestore.Exclude
import com.linc.inphoto.utils.extensions.EMPTY

data class MessageFirebaseModel(
    val userId: String,
    val text: String,
    val files: List<String>,
    val createdTimestamp: Long,
    val isSystem: Boolean,
    val isEdited: Boolean
) {
    companion object {
        @JvmStatic
        fun getChatMessageInstance(
            userId: String,
            text: String,
            files: List<String>
        ) = MessageFirebaseModel(
            userId,
            text,
            files,
            System.currentTimeMillis(),
            isSystem = false,
            isEdited = false
        )

        @JvmStatic
        fun getSystemMessageInstance() = MessageFirebaseModel(
            String.EMPTY,
            String.EMPTY,
            listOf(),
            System.currentTimeMillis(),
            isSystem = true,
            isEdited = false
        )
    }

    @Exclude
    @JvmField
    var id: String = String.EMPTY

}