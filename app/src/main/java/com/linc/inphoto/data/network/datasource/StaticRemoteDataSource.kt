package com.linc.inphoto.data.network.datasource

import com.linc.inphoto.BuildConfig
import javax.inject.Inject

class StaticRemoteDataSource @Inject constructor() {

    companion object {
        private const val PRIVACY_POLICY_PATH = "/static/privacy_policy.html"
        private const val TERMS_AND_CONDITIONS_PATH = "/static/terms_and_conditions.html"
    }

    fun getPrivacyPolicyUrl() = BuildConfig.BASE_URL + PRIVACY_POLICY_PATH

    fun getTermsAndConditionsUrl() = BuildConfig.BASE_URL + TERMS_AND_CONDITIONS_PATH

}