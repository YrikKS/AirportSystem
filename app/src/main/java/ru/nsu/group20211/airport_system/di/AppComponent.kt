package ru.nsu.group20211.airport_system.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.nsu.group20211.airport_system.MainActivity
import ru.nsu.group20211.airport_system.di.app_module.AppModule
import ru.nsu.group20211.airport_system.di.app_module.UserModule
import ru.nsu.group20211.airport_system.di.view_models.ViewModelModule
import ru.nsu.group20211.airport_system.presentation.employee.EmployeeFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        UserModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(target: EmployeeFragment)
    fun ingect(target: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }
}