package com.linc.inphoto.data.network.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.linc.inphoto.data.mapper.toMessageModel
import com.linc.inphoto.data.network.model.chat.MessageFirebaseModel
import com.linc.inphoto.data.network.model.media.RemoteMediaApiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
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

    suspend fun getChatMessages(chatId: String?) = callbackFlow {
        if (chatId.isNullOrEmpty()) error("Chat not found!")
        val listener = getMessagesCollection(chatId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                error(error)
            }
            trySend(snapshot?.documents?.map(DocumentSnapshot::toMessageModel).orEmpty())
        }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)

    suspend fun loadChatMessages(
        chatId: String?
    ): List<MessageFirebaseModel> = withContext(ioDispatcher) {
        if (chatId.isNullOrEmpty()) error("Chat not found!")
        return@withContext getMessagesCollection(chatId)
            .get()
            .await()
            .map(DocumentSnapshot::toMessageModel)
    }

    suspend fun loadChatMessage(
        chatId: String,
        messageId: String,
    ): MessageFirebaseModel = withContext(ioDispatcher) {
        return@withContext getMessagesCollection(chatId)
            .document(messageId)
            .get()
            .await()
            .toMessageModel()
    }

    suspend fun loadLastChatMessage(chatId: String): MessageFirebaseModel? =
        withContext(ioDispatcher) {
            return@withContext getMessagesCollection(chatId)
                .orderBy("createdTimestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
                .singleOrNull()
                ?.toMessageModel()
        }

    suspend fun sendChatMessages(
        chatId: String?,
        messageId: String,
        userId: String,
        text: String,
        attachments: List<RemoteMediaApiModel>
    ) = withContext(ioDispatcher) {
        if (chatId.isNullOrEmpty()) error("Chat not found!")
        val message = MessageFirebaseModel.getChatMessageInstance(userId, text, attachments)
        getMessagesCollection(chatId)
            .document(messageId)
            .set(message)
            .await()
    }

    suspend fun updateChatMessages(
        chatId: String?,
        messageId: String,
        text: String,
        files: List<String>,
    ) = withContext(ioDispatcher) {
        if (chatId.isNullOrEmpty()) error("Chat not found!")
        getMessagesCollection(chatId)
            .document(messageId)
            .update(
                "text", text,
                "files", files,
                "edited", true
            )
    }

    suspend fun deleteChatMessages(
        chatId: String?,
        messageId: String,
    ) = withContext(ioDispatcher) {
        if (chatId.isNullOrEmpty()) error("Chat not found!")
        getMessagesCollection(chatId)
            .document(messageId)
            .delete()
            .await()
    }

    private suspend fun getMessagesCollection(
        chatId: String
    ): CollectionReference = withContext(ioDispatcher) {
        return@withContext firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .collection(MESSAGES_COLLECTION)
    }


}