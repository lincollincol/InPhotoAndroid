package com.linc.inphoto.di.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
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
    fun provideGlobalCicerone(): Cicerone<Router> = Cicerone.create()

    @GlobalNavigatorHolder
    @Provides
    @Singleton
    fun provideGlobalRouter(
        @GlobalNavigatorHolder cicerone: Cicerone<Router>
    ): Router = cicerone.router

    @GlobalNavigatorHolder
    @Provides
    @Singleton
    fun provideGlobalNavigatorHolder(
        @GlobalNavigatorHolder cicerone: Cicerone<Router>
    ): NavigatorHolder = cicerone.getNavigatorHolder()

    @LocalNavigatorHolder
    @Provides
    @Singleton
    fun provideLocalCicerone(): Cicerone<Router> = Cicerone.create()

    @LocalNavigatorHolder
    @Provides
    @Singleton
    fun provideLocalRouter(
        @LocalNavigatorHolder cicerone: Cicerone<Router>
    ): Router = cicerone.router

    @LocalNavigatorHolder
    @Provides
    @Singleton
    fun provideLocalNavigatorHolder(
        @LocalNavigatorHolder cicerone: Cicerone<Router>
    ): NavigatorHolder = cicerone.getNavigatorHolder()

}