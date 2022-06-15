package com.linc.inphoto.data.repository

import com.linc.inphoto.data.android.LocalizationLocalDataSource
import com.linc.inphoto.data.android.RatioLocalDataSource
import com.linc.inphoto.data.network.datasource.StaticRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val ratioLocalDataSource: RatioLocalDataSource,
    private val staticRemoteDataSource: StaticRemoteDataSource,
    private val localizationLocalDataSource: LocalizationLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun loadAspectRatios() = ratioLocalDataSource.loadAspectRatios()

    suspend fun loadAvailableLanguages(): List<Locale> = withContext(ioDispatcher) {
        return@withContext localizationLocalDataSource.loadAvailableLanguages()
    }

    suspend fun loadPrivacyPolicyUrl(): String = withContext(ioDispatcher) {
        return@withContext staticRemoteDataSource.getPrivacyPolicyUrl()
    }

    suspend fun loadTermsAndConditionsUrl(): String = withContext(ioDispatcher) {
        return@withContext staticRemoteDataSource.getTermsAndConditionsUrl()
    }

}