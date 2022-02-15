package com.linc.inphoto.data.network.api

import com.rhythmoya.data.network.helper.HttpHelper.Header.MULTIPART_REQUEST
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApiService {

    @Headers(MULTIPART_REQUEST)
    @Multipart
    @POST("/users/update-avatar/{id}")
    suspend fun updateUserAvatar(
        @Part image: MultipartBody.Part,
        @Path("id") id: String
    )

}