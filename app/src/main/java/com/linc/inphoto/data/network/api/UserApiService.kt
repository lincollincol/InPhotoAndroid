package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.user.UserApiModel
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApiService {

    @Multipart
    @POST("/users/{userId}/avatar")
    suspend fun updateUserAvatar(
        @Path("userId") id: String,
        @Part image: MultipartBody.Part
    ): BaseResponse<UserApiModel>

    @Multipart
    @POST("/users/{userId}/header")
    suspend fun updateUserHeader(
        @Path("userId") id: String,
        @Part image: MultipartBody.Part
    ): BaseResponse<UserApiModel>

    @POST("/users/{userId}/avatar-url")
    suspend fun updateUserAvatarUrl(
        @Path("userId") id: String,
        @Body avatarUrl: String,
    ): BaseResponse<UserApiModel>

    @POST("/users/{userId}/header-url")
    suspend fun updateUserHeaderUrl(
        @Path("userId") id: String,
        @Body headerUrl: String
    ): BaseResponse<UserApiModel>

    @POST("/users/{userId}/username")
    suspend fun updateUserName(
        @Path("userId") id: String,
        @Body name: String
    ): BaseResponse<*>

    @POST("/users/{userId}/status")
    suspend fun updateUserStatus(
        @Path("userId") id: String,
        @Body status: String
    ): BaseResponse<*>

    @POST("/users/{userId}/gender")
    suspend fun updateUserGender(
        @Path("userId") id: String,
        @Body gender: String
    ): BaseResponse<*>

    @GET("/users")
    suspend fun getUsers(): BaseResponse<List<UserApiModel>>

    @GET("/users/{userId}")
    suspend fun getUserById(@Path("userId") id: String): BaseResponse<UserApiModel>

}