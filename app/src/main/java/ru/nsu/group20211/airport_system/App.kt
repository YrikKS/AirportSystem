package ru.nsu.group20211.airport_system

import android.app.Application
import android.content.Context
import ru.nsu.group20211.airport_system.di.AppComponent
import ru.nsu.group20211.airport_system.di.DaggerAppComponent
import java.util.Locale

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {

        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
