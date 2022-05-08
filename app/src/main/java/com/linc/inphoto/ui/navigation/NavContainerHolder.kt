package com.linc.inphoto.ui.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.linc.inphoto.di.navigation.GlobalNavigatorHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavContainerHolder @Inject constructor(
    @GlobalNavigatorHolder val globalContainer: Cicerone<AppRouter>
) {

    private val containers: MutableMap<String, Cicerone<AppRouter>> = hashMapOf()

    fun initContainers(tabs: Map<String, Cicerone<AppRouter>>) {
        this.containers.putAll(tabs)
    }

    fun initContainers(tabs: List<String>) {
        this.containers.putAll(tabs.associateWith { Cicerone.create(AppRouter()) })
    }

    fun initContainer(container: String) {
        this.containers.put(container, Cicerone.create(AppRouter()))
    }

    fun getRouter(id: String) =
//        containers[id]?.router ?: getGlobalRouter()//throw NullPointerException("Router not found!")
        containers[id]?.router ?: throw NullPointerException("Router not found!")

    fun setNavigator(id: String, navigator: Navigator) {
        containers[id]?.getNavigatorHolder()?.setNavigator(navigator)
    }

    fun removeNavigator(id: String) {
        containers[id]?.getNavigatorHolder()?.removeNavigator()
    }

    fun getGlobalRouter() = globalContainer.router
}