package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ContentApiService {

    @GET("/content/random-avatar")
    suspend fun getRandomAvatar(@Query("gender") gender: String?): BaseResponse<String>

    @GET("/content/random-header")
    suspend fun getRandomHeader(): BaseResponse<String>

}