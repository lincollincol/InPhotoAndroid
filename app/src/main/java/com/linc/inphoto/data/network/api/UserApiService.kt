package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.user.UserApiModel
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApiService {

    @POST("/users/{userId}/avatar")
    suspend fun updateUserAvatar(
        @Part image: MultipartBody.Part,
        @Path("userId") id: String
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

}