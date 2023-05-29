package ru.nsu.group20211.airport_system.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.presentation.GlobalNavigation
import ru.nsu.group20211.airportsystem.databinding.FragmentMainPlanesBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentMainScheduleBinding
import javax.inject.Inject

class ScheduleMainFragment : Fragment() {
    private lateinit var binding: FragmentMainScheduleBinding

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
        binding = FragmentMainScheduleBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        binding.flightSchedule.setOnClickListener {
            router.navigateTo(GlobalNavigation.FlightScheduleScreen())
        }
        binding.airports.setOnClickListener {
            router.navigateTo(GlobalNavigation.AirportScreen())
        }
        binding.approximateFlights.setOnClickListener {
            router.navigateTo(GlobalNavigation.ApproximateFlightScreen())
        }
        binding.technicalInspection.setOnClickListener {
            router.navigateTo(GlobalNavigation.TechnicalInspectionScreen())
        }
        binding.refueling.setOnClickListener {
            router.navigateTo(GlobalNavigation.RefuelingScreen())
        }
        binding.salonMaintenance.setOnClickListener {
            router.navigateTo(GlobalNavigation.SalonMaintenanceScreen())
        }
        binding.typeFuel.setOnClickListener {
            router.navigateTo(GlobalNavigation.TypeFuelScreen())
        }

    }

    companion object {
        fun newInstance() = ScheduleMainFragment()
    }

}