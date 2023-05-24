package ru.nsu.group20211.airport_system.di.app_module

import dagger.Module


@Module(includes = [DatabaseModule::class, NavigationModule::class, UrlModule::class])
class AppModule {
}