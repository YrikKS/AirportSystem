package ru.nsu.group20211.airport_system.di.app_module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton


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
    fun provideRouter(): Router {
        return globalCicerone.router
    }

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder {
        return globalCicerone.getNavigatorHolder()
    }
}

