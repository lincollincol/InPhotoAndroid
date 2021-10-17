package com.linc.inphoto.data.api.service

import com.linc.inphoto.data.api.dto.BaseResponse
import com.linc.inphoto.data.api.dto.auth.SignInRequest
import com.linc.inphoto.data.api.dto.auth.SignUpRequest
import com.linc.inphoto.data.api.dto.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/sign-in")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ) : BaseResponse<UserResponse>

    @POST("/auth/sign-up")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ) : BaseResponse<UserResponse>

}