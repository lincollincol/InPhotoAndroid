package com.linc.inphoto.data.network.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.getField
import com.linc.inphoto.data.network.model.chat.MessageFirebaseModel
import com.linc.inphoto.utils.getList
import com.linc.inphoto.utils.getTimestampMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessagesCollection @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val CHATS_COLLECTION = "chats"
        private const val MESSAGES_COLLECTION = "messages"
    }

    suspend fun loadChatMessages(
        chatId: String
    ): List<MessageFirebaseModel> = withContext(ioDispatcher) {
        return@withContext getMessagesCollection(chatId)
            .get()
            .await()
            .map(::getMessageFirebaseModel)
    }

    suspend fun loadChatMessage(
        chatId: String,
        messageId: String,
    ): MessageFirebaseModel = withContext(ioDispatcher) {
        return@withContext getMessagesCollection(chatId)
            .document(messageId)
            .get()
            .await()
            .let(::getMessageFirebaseModel)
    }

    suspend fun loadLastChatMessage(chatId: String): MessageFirebaseModel =
        withContext(ioDispatcher) {
            return@withContext getMessagesCollection(chatId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
                .single()
                .let(::getMessageFirebaseModel)
        }

    private suspend fun getMessagesCollection(
        chatId: String
    ): CollectionReference = withContext(ioDispatcher) {
        return@withContext firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .collection(MESSAGES_COLLECTION)
    }

    private fun getMessageFirebaseModel(document: DocumentSnapshot) = MessageFirebaseModel(
        id = document.id,
        userId = document.getField<String>("userId").orEmpty(),
        text = document.getField<String>("text").orEmpty(),
        files = document.getList("files"),
        createdTimestamp = document.getTimestampMillis("createdAt"),
        isSystem = document.getField("isSystem") ?: false
    )
}