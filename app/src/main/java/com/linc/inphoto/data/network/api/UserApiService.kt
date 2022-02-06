package com.linc.inphoto.data.network.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApiService {

    @Multipart
    @POST("/users/update-avatar/{id}")
    suspend fun updateUserAvatar(
        @Part image: MultipartBody.Part,
        @Path("id") id: String
    )

}