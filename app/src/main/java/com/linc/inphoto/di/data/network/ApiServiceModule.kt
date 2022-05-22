package com.linc.inphoto.di.data.network

import com.linc.inphoto.data.network.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun providePostApiService(retrofit: Retrofit): PostApiService =
        retrofit.create(PostApiService::class.java)

    @Provides
    @Singleton
    fun provideContentApiService(retrofit: Retrofit): ContentApiService =
        retrofit.create(ContentApiService::class.java)

    @Provides
    @Singleton
    fun provideTagApiService(retrofit: Retrofit): TagApiService =
        retrofit.create(TagApiService::class.java)

    @Provides
    @Singleton
    fun provideStoryApiService(retrofit: Retrofit): StoryApiService =
        retrofit.create(StoryApiService::class.java)

}