package ru.nsu.group20211.airport_system.di.app_module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
annotation class GlobalNavigation

@Qualifier
annotation class BottomNavigation

@Module(
    includes = [
        GlobalNavigationModule::class,
    ]
)
class NavigationModule

@Module
class GlobalNavigationModule {
    private val globalCicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    @GlobalNavigation
    fun provideRouter(): Router {
        return globalCicerone.router
    }

    @Provides
    @Singleton
    @GlobalNavigation
    fun provideNavigatorHolder(): NavigatorHolder {
        return globalCicerone.getNavigatorHolder()
    }
}

