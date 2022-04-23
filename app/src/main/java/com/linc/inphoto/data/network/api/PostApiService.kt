package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
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

    @GET("/posts/users/{userId}")
    suspend fun getUserPosts(
        @Path("userId") userId: String
    ): BaseResponse<List<PostApiModel>>

    @GET("/posts-extended/{userId}")
    suspend fun getExtendedUserPosts(
        @Path("userId") userId: String
    ): BaseResponse<List<ExtendedPostApiModel>>

    @GET("/posts-extended/{postId}/{userId}")
    suspend fun getExtendedPost(
        @Path("postId") postId: String,
        @Path("userId") userId: String
    ): BaseResponse<ExtendedPostApiModel>

    @GET("/posts-extended/{userId}")
    suspend fun getExtendedPosts(
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

}