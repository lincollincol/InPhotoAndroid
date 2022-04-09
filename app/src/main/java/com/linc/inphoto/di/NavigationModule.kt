package com.linc.inphoto.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.navigation.AppRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create()
    }

    @Provides
    @Singleton
    fun provideAppCicerone(): Cicerone<AppRouter> {
        return Cicerone.create(AppRouter())
    }

    @Provides
    @Singleton
    fun provideRouter(cicerone: Cicerone<Router>): Router {
        return cicerone.router
    }

    @Provides
    @Singleton
    fun provideDialogRouter(cicerone: Cicerone<AppRouter>): AppRouter {
        return cicerone.router
    }

    @Provides
    @Singleton
    fun provideAppNavigatorHolder(cicerone: Cicerone<AppRouter>): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

}