package ru.nsu.group20211.airport_system.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.presentation.GlobalNavigation
import ru.nsu.group20211.airportsystem.databinding.FragmentMainTicketBinding
import javax.inject.Inject

class TicketMainFragment : Fragment() {
    private lateinit var binding: FragmentMainTicketBinding

    @Inject
    lateinit var router: Router


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
//        model = ViewModelProvider(this, modelFactory)[AircraftRepairReportViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainTicketBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        binding.tickets.setOnClickListener {
            router.navigateTo(GlobalNavigation.TicketScreen())
        }
        binding.ticketsHandedOver.isVisible = false
//        binding.ticketsHandedOver.setOnClickListener {
//            router.navigateTo(GlobalNavigation.EmployeeScreen())
//        }
        binding.passengers.setOnClickListener {
            router.navigateTo(GlobalNavigation.PassengerScreen())
        }
    }

    companion object {
        fun newInstance() = TicketMainFragment()
    }

}