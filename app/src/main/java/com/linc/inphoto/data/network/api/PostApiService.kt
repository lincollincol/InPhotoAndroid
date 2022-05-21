package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.post.CommentApiModel
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import com.linc.inphoto.data.network.model.post.UpdatePostApiModel
import com.linc.inphoto.data.network.model.user.UserApiModel
import com.linc.inphoto.data.network.utils.HttpHelper
import okhttp3.MultipartBody
import retrofit2.http.*

interface PostApiService {

    @Headers(HttpHelper.Header.MULTIPART_REQUEST)
    @Multipart
    @POST("/posts/{userId}")
    suspend fun savePost(
        @Part image: MultipartBody.Part,
        @Part description: MultipartBody.Part,
        @Part tags: List<MultipartBody.Part>,
        @Path("userId") id: String
    ): BaseResponse<UserApiModel>

    @PUT("/posts/{postId}")
    suspend fun updatePost(
        @Path("postId") id: String,
        @Body body: UpdatePostApiModel
    ): BaseResponse<*>

    @GET("/posts/tags/{tagId}")
    suspend fun getTagPosts(
        @Path("tagId") tagId: String?
    ): BaseResponse<List<PostApiModel>>

    @GET("/posts-extended/tags/{tagId}")
    suspend fun getExtendedTagPosts(
        @Path("tagId") tagId: String?,
        @Query("userId") userId: String?,
    ): BaseResponse<List<ExtendedPostApiModel>>

    @GET("/posts/users/{userId}")
    suspend fun getUserPosts(
        @Path("userId") userId: String
    ): BaseResponse<List<PostApiModel>>

    @GET("/posts-extended/users/{userId}")
    suspend fun getExtendedUserPosts(
        @Path("userId") userId: String
    ): BaseResponse<List<ExtendedPostApiModel>>

    @GET("/posts-extended/{postId}/{userId}")
    suspend fun getExtendedPost(
        @Path("postId") postId: String,
        @Path("userId") userId: String
    ): BaseResponse<ExtendedPostApiModel>

    @GET("/posts-extended/{userId}")
    suspend fun getAllExtendedPosts(
        @Path("userId") userId: String
    ): BaseResponse<List<ExtendedPostApiModel>>

    @GET("/posts-extended/users-following/{userId}")
    suspend fun getUserFollowingExtendedPosts(
        @Path("userId") userId: String
    ): BaseResponse<List<ExtendedPostApiModel>>

    @POST("/posts/{postId}/like/{userId}")
    suspend fun likePost(
        @Path("postId") postId: String,
        @Path("userId") userId: String,
        @Query("liked") bookmarked: Boolean
    ): BaseResponse<ExtendedPostApiModel>

    @POST("/posts/{postId}/bookmark/{userId}")
    suspend fun bookmarkPost(
        @Path("postId") postId: String,
        @Path("userId") userId: String,
        @Query("bookmarked") bookmarked: Boolean
    ): BaseResponse<ExtendedPostApiModel>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): BaseResponse<*>

    @GET("/posts/{postId}/comments")
    suspend fun getPostComments(
        @Path("postId") postId: String
    ): BaseResponse<List<CommentApiModel>>

    @POST("/posts/{postId}/comments/{userId}")
    suspend fun commentPost(
        @Path("postId") postId: String,
        @Path("userId") userId: String,
        @Body comment: String
    ): BaseResponse<CommentApiModel>

    @PUT("/posts/comments/{commentId}")
    suspend fun updatePostComment(
        @Path("commentId") commentId: String,
        @Body comment: String
    ): BaseResponse<*>

    @DELETE("/posts/comments/{commentId}")
    suspend fun deletePostComment(
        @Path("commentId") commentId: String,
    ): BaseResponse<*>

}