package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ContentApiService {

    @GET("/content/random-avatar")
    suspend fun getRandomAvatar(@Query("gender") gender: String?): BaseResponse<String>

    @GET("/content/random-header")
    suspend fun getRandomHeader(): BaseResponse<String>

    @GET("/content/stickers")
    suspend fun getStickers(): BaseResponse<List<String>>

    @Multipart
    @POST("/content/chat-file")
    suspend fun uploadChatContent(@Part image: MultipartBody.Part): BaseResponse<String>

}