package com.linc.inphoto.di.data

import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.data.storage.database.LocalDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCoroutineDispatcher() : CoroutineDispatcher = Dispatchers.IO

}