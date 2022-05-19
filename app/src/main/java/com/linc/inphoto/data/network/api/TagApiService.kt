package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.post.TagApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface TagApiService {

    @GET("/tags")
    suspend fun getTags(@Query("query") query: String): BaseResponse<List<TagApiModel>>

}