package com.linc.inphoto.data.repository

import com.linc.inphoto.data.api.AuthService
import com.linc.inphoto.data.storage.LocalPreferences
import com.linc.inphoto.data.storage.database.dao.UserDao
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val localPreferences: LocalPreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun signIn() = withContext(dispatcher) {
        authService.signIn()
    }

    suspend fun signUp() = withContext(dispatcher) {
        authService.signUp()
    }

}