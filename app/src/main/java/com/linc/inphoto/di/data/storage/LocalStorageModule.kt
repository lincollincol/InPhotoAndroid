package com.linc.inphoto.di.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.linc.inphoto.data.database.LocalDatabase
import com.linc.inphoto.data.preferences.BasePreferences
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
        BasePreferences.PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

}