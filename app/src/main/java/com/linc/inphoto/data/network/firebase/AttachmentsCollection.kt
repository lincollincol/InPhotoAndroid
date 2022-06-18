package com.linc.inphoto.data.network.firebase

/*
class AttachmentsCollection @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val ATTACHMENTS_COLLECTION = "attachments"
    }

    suspend fun loadAttachment(
        attachmentId: String
    ): AttachmentFirebaseModel = withContext(ioDispatcher) {
        return@withContext firestore.collection(ATTACHMENTS_COLLECTION)
            .document(attachmentId)
            .get()
            .await()
            .toAttachmentModel()
    }

    suspend fun createAttachment(
        url: String,
        mime: String,
    ): String = withContext(ioDispatcher) {
        val attachmentId = UUID.randomUUID().toString()
        firestore.collection(ATTACHMENTS_COLLECTION)
            .document(attachmentId)
            .set(AttachmentFirebaseModel(url, mime))
            .await()
        return@withContext attachmentId
    }

    suspend fun deleteChat(attachmentId: String?) = withContext(ioDispatcher) {
        if (attachmentId.isNullOrEmpty()) {
            error("Attachment not found!")
        }
        firestore.collection(ATTACHMENTS_COLLECTION)
            .document(attachmentId)
            .delete()
            .await()
    }


}
*/
