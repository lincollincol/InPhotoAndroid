package com.linc.inphoto.data.api.service

import com.linc.inphoto.data.api.model.BaseResponse
import com.linc.inphoto.data.api.model.auth.SignInApiModel
import com.linc.inphoto.data.api.model.auth.SignUpApiModel
import com.linc.inphoto.data.api.model.user.UserApiModel
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/sign-in")
    suspend fun signIn(
        @Body signInApiModel: SignInApiModel
    ): BaseResponse<UserApiModel>

    @POST("/auth/sign-up")
    suspend fun signUp(
        @Body signUpApiModel: SignUpApiModel
    ): BaseResponse<UserApiModel>

}