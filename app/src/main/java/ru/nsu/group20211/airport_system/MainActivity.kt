package ru.nsu.group20211.airport_system

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.nsu.group20211.airport_system.presentation.GlobalNavigation
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: ActivityMainBinding
    private val navigator by lazy {
        AppNavigator(
            this,
            R.id.main_container,
            supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        appComponent.inject(this)

        if (savedInstanceState == null)
            router.replaceScreen(GlobalNavigation.EmployeeMainScreen())
        setupUi()
        setContentView(binding.root)
    }


    override fun onResume() {
        navigatorHolder.setNavigator(navigator)
        super.onResume()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun setupUi() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_action_employees -> {
                    router.newRootScreen(
                        GlobalNavigation.EmployeeMainScreen()
                    )
                    true
                }

                R.id.menu_action_plane -> {
                    router.newRootScreen(
                        GlobalNavigation.PlanesMainScreen()
                    )
                    true
                }

                R.id.menu_action_schedule -> {
                    router.newRootScreen(
                        GlobalNavigation.ScheduleMainScreen()
                    )
                    true
                }

                R.id.menu_action_ticket -> {
                    router.newRootScreen(
                        GlobalNavigation.TicketMainScreen()
                    )
                    true
                }

                else -> false
            }
        }
    }

}