package com.linc.inphoto.data.network.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.linc.inphoto.data.mapper.toChatModel
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ChatsCollection @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val CHATS_COLLECTION = "chats"
    }

    suspend fun getUserChats(userId: String?) = callbackFlow {
        if (userId.isNullOrEmpty()) {
            error("User chats not found!")
        }
        val listener = firestore.collection(CHATS_COLLECTION)
            .whereArrayContains("participants", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error(error)
                }
                trySend(snapshot?.documents?.map(DocumentSnapshot::toChatModel).orEmpty())
            }
        awaitClose { listener.remove() }
    }.flowOn(ioDispatcher)

    suspend fun loadUserChats(userId: String): List<ChatFirebaseModel> = withContext(ioDispatcher) {
        return@withContext firestore.collection(CHATS_COLLECTION)
            .whereArrayContains("participants", userId)
            .get()
            .await()
            .map(DocumentSnapshot::toChatModel)
    }

    suspend fun loadChat(chatId: String): ChatFirebaseModel = withContext(ioDispatcher) {
        return@withContext firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .get()
            .await()
            .toChatModel()
    }

    suspend fun createChat(
        participants: List<String?>
    ): String = withContext(ioDispatcher) {
        if (participants.filterNotNull().count() <= 1) {
            error("Cannot create chat! User not found!")
        }
        val chatId = UUID.randomUUID().toString()
        firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .set(ChatFirebaseModel(participants.filterNotNull()))
            .await()
        return@withContext chatId
    }

    suspend fun deleteChat(chatId: String?) = withContext(ioDispatcher) {
        if (chatId.isNullOrEmpty()) {
            error("Chat not found!")
        }
        firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .delete()
            .await()
    }

}