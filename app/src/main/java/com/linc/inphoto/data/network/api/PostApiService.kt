package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import retrofit2.http.GET
import retrofit2.http.Path

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

}