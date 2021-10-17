package com.linc.inphoto.di.data.storage

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.linc.inphoto.data.storage.LocalPreferences
import com.linc.inphoto.data.storage.database.LocalDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context
    ) : LocalDatabase = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        LocalDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ) : SharedPreferences = context.getSharedPreferences(
        LocalPreferences.PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

}