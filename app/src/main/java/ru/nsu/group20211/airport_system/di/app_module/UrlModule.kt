package ru.nsu.group20211.airport_system.di.app_module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UrlModule {
    @Singleton
    @Provides
    fun provideUrl(): UrlProvider {
        return UrlProvider()
    }

    class UrlProvider {
        val url = "jdbc:oracle:thin:@" + "84.237.50.81:1521" + ":XE"
    }
}