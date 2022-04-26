package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.mapper.toCommentModel
import com.linc.inphoto.data.mapper.toExtendedPostModel
import com.linc.inphoto.data.mapper.toPostModel
import com.linc.inphoto.data.network.api.PostApiService
import com.linc.inphoto.data.network.model.post.CommentApiModel
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import com.linc.inphoto.data.network.model.post.UpdatePostApiModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.post.Comment
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.utils.extensions.toMultipartBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postApiService: PostApiService,
    private val authPreferences: AuthPreferences,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun saveUserPost(
        uri: Uri?,
        description: String,
        tags: List<String>
    ) = withContext(ioDispatcher) {
        val image = mediaLocalDataSource.createTempFile(uri) ?: return@withContext null
        val descriptionPart = MultipartBody.Part.createFormData("description", description)
        postApiService.savePost(
            image.toMultipartBody(),
            descriptionPart,
            tags.map { MultipartBody.Part.createFormData("tag", it) },
            authPreferences.userId
        )
        image.delete()
    }

    suspend fun updateUserPost(
        postId: String,
        description: String,
        tags: List<String>
    ) = withContext(ioDispatcher) {
        postApiService.updatePost(postId, UpdatePostApiModel(description, tags))
    }

    suspend fun getCurrentUserPosts(): List<Post> = withContext(ioDispatcher) {
        val response = postApiService.getUserPosts(authPreferences.userId)
        return@withContext response.body?.map(PostApiModel::toPostModel).orEmpty()
    }

    suspend fun getCurrentUserExtendedPosts(): List<ExtendedPost> = withContext(ioDispatcher) {
        val response = postApiService.getExtendedUserPosts(authPreferences.userId)
        return@withContext response.body?.map(ExtendedPostApiModel::toExtendedPostModel).orEmpty()
    }

    suspend fun getUserExtendedPosts(userId: String): List<ExtendedPost> =
        withContext(ioDispatcher) {
            val response = postApiService.getExtendedUserPosts(userId)
            return@withContext response.body
                ?.map(ExtendedPostApiModel::toExtendedPostModel)
                .orEmpty()
        }

    suspend fun getExtendedPost(postId: String): ExtendedPost? = withContext(ioDispatcher) {
        val response = postApiService.getExtendedPost(postId, authPreferences.userId)
        return@withContext response.body?.toExtendedPostModel()
    }

    suspend fun getExtendedPosts(): List<ExtendedPost> = withContext(ioDispatcher) {
        val response = postApiService.getExtendedPosts(authPreferences.userId)
        return@withContext response.body
            ?.map(ExtendedPostApiModel::toExtendedPostModel)
            .orEmpty()
    }

    suspend fun likePost(
        postId: String,
        isLiked: Boolean
    ): ExtendedPost? = withContext(ioDispatcher) {
        return@withContext postApiService.likePost(
            postId,
            authPreferences.userId,
            isLiked
        ).body?.toExtendedPostModel()
    }

    suspend fun bookmarkPost(
        postId: String,
        isBookmarked: Boolean
    ): ExtendedPost? = withContext(ioDispatcher) {
        return@withContext postApiService.bookmarkPost(
            postId,
            authPreferences.userId,
            isBookmarked
        ).body?.toExtendedPostModel()
    }

    suspend fun deletePost(postId: String) = withContext(ioDispatcher) {
        postApiService.deletePost(postId)
    }

    suspend fun commentPost(postId: String) = withContext(ioDispatcher) {
        postApiService.commentPost(postId, authPreferences.userId)
    }

    suspend fun deletePostComment(commentId: String) = withContext(ioDispatcher) {
        postApiService.deletePostComment(commentId)
    }

    suspend fun getPostComments(postId: String): List<Comment> = withContext(ioDispatcher) {
        return@withContext postApiService.getPostComments(postId).body
            ?.map(CommentApiModel::toCommentModel)
            .orEmpty()
    }

}