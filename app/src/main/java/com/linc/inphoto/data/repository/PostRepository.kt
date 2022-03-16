package com.linc.inphoto.data.repository

import com.linc.inphoto.data.mapper.toExtendedPostModel
import com.linc.inphoto.data.mapper.toPostModel
import com.linc.inphoto.data.network.api.PostApiService
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.entity.post.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postApiService: PostApiService,
    private val authPreferences: AuthPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

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
            return@withContext response.body?.map(ExtendedPostApiModel::toExtendedPostModel)
                .orEmpty()
        }

    suspend fun getCurrentUserExtendedPost(
        postId: String
    ): ExtendedPost? = withContext(ioDispatcher) {
        val response = postApiService.getExtendedPost(authPreferences.userId, postId)
        return@withContext response.body?.toExtendedPostModel()
    }

}