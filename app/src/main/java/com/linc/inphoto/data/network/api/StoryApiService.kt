package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.story.StoryApiModel
import com.linc.inphoto.data.network.model.story.UserStoryApiModel
import okhttp3.MultipartBody
import retrofit2.http.*

interface StoryApiService {

    @Multipart
    @POST("/stories/users/{userId}")
    suspend fun createUserStory(
        @Path("userId") id: String,
        @Part image: MultipartBody.Part,
        @Part expiresTimestamp: MultipartBody.Part,
        @Part durationMillis: MultipartBody.Part,
    ): BaseResponse<*>

    @GET("/stories/users/{userId}")
    suspend fun getUserStories(
        @Path("userId") id: String
    ): BaseResponse<UserStoryApiModel>

    @GET("/stories/users-following/{userId}")
    suspend fun getUserFollowingStories(
        @Path("userId") id: String
    ): BaseResponse<List<UserStoryApiModel>>

}