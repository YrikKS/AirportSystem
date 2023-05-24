package ru.nsu.group20211.airport_system.di.app_module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule {
    @Provides
    @Singleton
    fun provideUserHolder(): UserHolder {
        return UserHolder()
    }


    class UserHolder {
        val userName = "20211_KURGIN"
        val pass = "Zhrehu19"
    }
}