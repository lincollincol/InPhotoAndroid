package com.linc.inphoto.ui.chatmessages.model

import android.os.Parcelable
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.user.User
import kotlinx.android.parcel.Parcelize

sealed class ConversationParams : Parcelable {

    companion object {
        @JvmStatic
        fun fromChat(chat: Chat) =
            Existent(chat.userId, chat.username, chat.userAvatarUrl, chat.id)

        @JvmStatic
        fun fromUser(user: User) = New(user.id, user.name, user.avatarUrl)

        @JvmStatic
        fun undefined(user: User) = Undefined(user.id, user.name, user.avatarUrl)
    }

    abstract val userId: String
    abstract val username: String
    abstract val avatarUrl: String

    @Parcelize
    class Existent(
        override val userId: String,
        override val username: String,
        override val avatarUrl: String,
        val chatId: String
    ) : ConversationParams()

    @Parcelize
    class New(
        override val userId: String,
        override val username: String,
        override val avatarUrl: String
    ) : ConversationParams()

    @Parcelize
    class Undefined(
        override val userId: String,
        override val username: String,
        override val avatarUrl: String
    ) : ConversationParams()
}