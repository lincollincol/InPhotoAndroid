package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

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

}