package com.linc.inphoto.di.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.linc.inphoto.ui.navigation.AppRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @GlobalNavigatorHolder
    @Provides
    @Singleton
    fun provideGlobalCicerone(): Cicerone<AppRouter> = Cicerone.create(AppRouter())

    @GlobalNavigatorHolder
    @Provides
    @Singleton
    fun provideGlobalRouter(
        @GlobalNavigatorHolder cicerone: Cicerone<AppRouter>
    ): AppRouter = cicerone.router

    @GlobalNavigatorHolder
    @Provides
    @Singleton
    fun provideGlobalNavigatorHolder(
        @GlobalNavigatorHolder cicerone: Cicerone<AppRouter>
    ): NavigatorHolder = cicerone.getNavigatorHolder()

    @LocalNavigatorHolder
    @Provides
    @Singleton
    fun provideLocalCicerone(): Cicerone<AppRouter> = Cicerone.create(AppRouter())

    @LocalNavigatorHolder
    @Provides
    @Singleton
    fun provideLocalRouter(
        @LocalNavigatorHolder cicerone: Cicerone<AppRouter>
    ): AppRouter = cicerone.router

    @LocalNavigatorHolder
    @Provides
    @Singleton
    fun provideLocalNavigatorHolder(
        @LocalNavigatorHolder cicerone: Cicerone<AppRouter>
    ): NavigatorHolder = cicerone.getNavigatorHolder()

}