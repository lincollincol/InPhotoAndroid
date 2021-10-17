package com.linc.inphoto.data.api

import retrofit2.http.POST

interface AuthService {

    @POST("/auth/sign-in")
    suspend fun signIn()

    @POST("/auth/sign-up")
    suspend fun signUp()

}