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
import javax.inject.Inject

class PlanesMainFragment : Fragment() {
    private lateinit var binding: FragmentMainPlanesBinding

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
        binding = FragmentMainPlanesBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        binding.plane.setOnClickListener {
            router.navigateTo(GlobalNavigation.PlaneScreen())
        }
        binding.modelPlanes.setOnClickListener {
            router.navigateTo(GlobalNavigation.ModelPlaneScreen())
        }
        binding.aircraftRepairReports.setOnClickListener {
            router.navigateTo(GlobalNavigation.AircraftRepairReportScreen())
        }
        binding.largeTechnicalInspection.setOnClickListener {
            router.navigateTo(GlobalNavigation.LargeTechnicalInspectionScreen())
        }
    }

    companion object {
        fun newInstance() = PlanesMainFragment()
    }

}