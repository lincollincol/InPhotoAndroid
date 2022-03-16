package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.user.UserApiModel
import com.rhythmoya.data.network.helper.HttpHelper.Header.MULTIPART_REQUEST
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApiService {

    @Headers(MULTIPART_REQUEST)
    @Multipart
    @POST("/users/{userId}/avatar")
    suspend fun updateUserAvatar(
        @Part image: MultipartBody.Part,
        @Path("userId") id: String
    ): BaseResponse<UserApiModel>

}