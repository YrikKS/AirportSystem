package ru.nsu.group20211.airport_system.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.presentation.GlobalNavigation
import ru.nsu.group20211.airportsystem.databinding.FragmentMainEmployeeBinding
import javax.inject.Inject

class EmployeeMainFragment : Fragment() {
    private lateinit var binding: FragmentMainEmployeeBinding

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
        binding = FragmentMainEmployeeBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        binding.human.setOnClickListener {
            router.navigateTo(GlobalNavigation.HumanScreen())
        }
        binding.employee.setOnClickListener {
            router.navigateTo(GlobalNavigation.EmployeeScreen())
        }
        binding.depatment.setOnClickListener {
            router.navigateTo(GlobalNavigation.DepartmentScreen())
        }
        binding.brigades.setOnClickListener {
            router.navigateTo(GlobalNavigation.BrigadeScreen())
        }
        binding.employeeClass.setOnClickListener {
            router.navigateTo(GlobalNavigation.EmployeeClassScreen())
        }
    }

    companion object {
        fun newInstance() = EmployeeMainFragment()
    }

}