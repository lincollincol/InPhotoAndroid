package com.linc.inphoto.di.device

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import linc.com.amplituda.Amplituda
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideAmplituda(@ApplicationContext context: Context): Amplituda = Amplituda(context)

}