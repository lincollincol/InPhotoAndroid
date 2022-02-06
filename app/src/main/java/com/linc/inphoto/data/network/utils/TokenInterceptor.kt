package com.rhythmoya.data.network.helper

import com.linc.inphoto.data.preferences.AuthPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().apply {
            if (!authPreferences.accessToken.isNullOrEmpty()) {
                addHeader(
                    HttpHelper.Header.AUTHORIZATION,
                    HttpHelper.bearer(authPreferences.accessToken!!)
                )
            }
        }.build()
        return chain.proceed(newRequest)
    }
}