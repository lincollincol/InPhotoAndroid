package com.linc.inphoto.data.network.api

import com.linc.inphoto.data.network.model.BaseResponse
import com.linc.inphoto.data.network.model.auth.RefreshApiModel
import com.linc.inphoto.data.network.model.auth.SignInApiModel
import com.linc.inphoto.data.network.model.auth.SignUpApiModel
import com.linc.inphoto.data.network.model.user.UserApiModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/sign-in")
    suspend fun signIn(
        @Body signInApiModel: SignInApiModel
    ): BaseResponse<UserApiModel>

    @POST("/auth/sign-up")
    suspend fun signUp(
        @Body signUpApiModel: SignUpApiModel
    ): BaseResponse<UserApiModel>

    @POST("/refresh")
    fun refresh(
        @Body refreshApiModel: RefreshApiModel
    ): Call<BaseResponse<RefreshApiModel>>

}