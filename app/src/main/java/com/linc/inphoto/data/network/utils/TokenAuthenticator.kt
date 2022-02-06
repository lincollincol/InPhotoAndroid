package com.linc.inphoto.data.network.utils

import com.linc.inphoto.data.network.api.AuthApiService
import com.linc.inphoto.data.network.model.auth.RefreshApiModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.rhythmoya.data.network.helper.HttpHelper
import dagger.Lazy
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val authApiService: Lazy<AuthApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            if (authApiService.get() == null || authPreferences.refreshToken.isEmpty()) {
                return null
            }

            val requestBodyModel = RefreshApiModel(
                authPreferences.accessToken,
                authPreferences.refreshToken
            )
            val refreshResult = authApiService.get().refresh(requestBodyModel)
                .execute()
                .body()
                ?.body
                ?: return null

            with(authPreferences) {
                accessToken = refreshResult.accessToken
                refreshToken = refreshResult.refreshToken
            }

            return response.request.newBuilder()
                .header(
                    HttpHelper.Header.AUTHORIZATION,
                    HttpHelper.bearer(authPreferences.accessToken)
                )
                .build()
        }
    }
}