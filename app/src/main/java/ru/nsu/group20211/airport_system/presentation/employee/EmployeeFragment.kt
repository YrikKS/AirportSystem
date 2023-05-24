package ru.nsu.group20211.airport_system.presentation.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airportsystem.R
import javax.inject.Inject

class EmployeeFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var model: EmployeeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[EmployeeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.getSomething()
        return inflater.inflate(R.layout.fragment_employee, container, false)
    }

    companion object {
        fun newInstance() =
            EmployeeFragment()
    }
}