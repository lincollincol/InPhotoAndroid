package com.linc.inphoto.di.data.storage

import com.linc.inphoto.data.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideUserDao(
        localDatabase: LocalDatabase
    ) = localDatabase.userDao

}