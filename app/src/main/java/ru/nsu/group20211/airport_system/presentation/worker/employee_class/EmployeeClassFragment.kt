package ru.nsu.group20211.airport_system.presentation.worker.employee_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.nsu.group20211.airport_system.addInsertButton
import ru.nsu.group20211.airport_system.addInsertPickField
import ru.nsu.group20211.airport_system.addInsertPickFieldFromList
import ru.nsu.group20211.airport_system.addInsertTextField
import ru.nsu.group20211.airport_system.addUpdateButton
import ru.nsu.group20211.airport_system.addUpdatePickField
import ru.nsu.group20211.airport_system.addUpdatePickFieldFromList
import ru.nsu.group20211.airport_system.addUpdateTextField
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentEmployeeClassBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import javax.inject.Inject

class EmployeeClassFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: EmployeeClassViewModel
    private lateinit var binding: FragmentEmployeeClassBinding
    private lateinit var adapter: EmployeeClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[EmployeeClassViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeClassBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }


    private fun setupUi() {
        adapter = EmployeeClassAdapter(emptyList()) { createDialog(it) }
        binding.recycleEmployee.adapter = adapter
        binding.recycleEmployee.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleEmployee.addItemDecoration(SpaceItemDecorator(3))
        binding.floatingEmployee.setOnClickListener {
            openDialogForPickEmployee()
        }
        binding.buttonSide.setOnClickListener {
            openSideDialog()
        }


        model.stateProvider.onEach {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)


        model.errorProvider.onEach {
            it.printStackTrace()
            Snackbar.make(
                requireContext(), binding.root, it.message ?: "", Snackbar.LENGTH_SHORT
            ).setAction("Refresh") {
                model.getData()
            }.show()
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)
        model.getData()
    }

    private fun openDialogForPickEmployee() {
        val items = arrayOf(
            "Administrators",
            "Security",
            "Other employees",
            "Dispatchers",
            "Techniques",
            "Cashiers",
            "Pilots"
        )
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog_App
        ).setItems(items) { dialog, which ->
            when (which) {
                0 -> openBottomDialogInsert(EmployeeClass.Administrator())
                1 -> openBottomDialogInsert(EmployeeClass.Security())
                2 -> openBottomDialogInsert(EmployeeClass.Other())
                3 -> openBottomDialogInsert(EmployeeClass.Dispatchers())
                4 -> openBottomDialogInsert(EmployeeClass.Techniques())
                5 -> openBottomDialogInsert(EmployeeClass.Cashiers())
                6 -> openBottomDialogInsert(EmployeeClass.Pilots())
            }
            dialog.dismiss()
        }.show()
    }

    private fun generateInitTextField(employeeClass: EmployeeClass): List<Pair<String, (String) -> Unit>> {
        return when (employeeClass) {
            is EmployeeClass.Administrator -> listOf<Pair<String, (String) -> Unit>>()
            is EmployeeClass.Cashiers -> listOf("Number of language" to {
                employeeClass.numberLanguages = it.toInt()
            })

            is EmployeeClass.Dispatchers -> listOf("Number of language" to {
                employeeClass.numberLanguages = it.toInt()
            })

            is EmployeeClass.Other -> listOf("Type worker" to { employeeClass.typeWorker = it })

            is EmployeeClass.Pilots -> listOf(

            )

            is EmployeeClass.Security -> {
                listOf()
            }

            is EmployeeClass.Techniques -> {
                listOf()
            }
        }
    }

    private fun generateInitPickField(employeeClass: EmployeeClass): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(Triple("Employee",
            { employeeClass.employee = it.customGetId() },
            suspend { model.getEmployees() } to { (it as Employee).human!!.name + " " + it.human!!.surname })
        )
    }

    private fun generateInitPickFieldFromList(employeeClass: EmployeeClass): List<Triple<String, (String) -> Unit, List<String>>> {
        return when (employeeClass) {
            is EmployeeClass.Administrator -> listOf()
            is EmployeeClass.Cashiers -> listOf()
            is EmployeeClass.Dispatchers -> listOf()
            is EmployeeClass.Other -> listOf()
            is EmployeeClass.Pilots -> {
                listOf(
                    Triple(
                        "Rating",
                        { employeeClass.rating = it },
                        listOf("Instrument Rating", "Multi-Engine Rating")
                    ),
                    Triple(
                        "License category", { employeeClass.licenseCategory = it }, listOf(
                            "Airline Transport Pilot",
                            "Commercial Pilot",
                            "Senior Private Pilot",
                            "Private Pilot"
                        )
                    ),
                )
            }

            is EmployeeClass.Security -> {
                listOf(
                    Triple(
                        "Weapons permit",
                        { employeeClass.weaponsPermission = it == "Yes" },
                        listOf("Yes", "No")
                    ),
                    Triple(
                        "Military service",
                        { employeeClass.militaryService = it == "Yes" },
                        listOf("Yes", "No")
                    ),
                )
            }

            is EmployeeClass.Techniques -> {
                listOf(
                    Triple(
                        "Qualification",
                        { employeeClass.qualification = it },
                        listOf("engines", "electronics", "refill", "wheels")
                    )
                )
            }
        }
    }

    private fun generateUpdateTextField(
        employeeClass: EmployeeClass, newEmployeeClass: EmployeeClass
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return when (employeeClass) {
            is EmployeeClass.Administrator -> listOf()
            is EmployeeClass.Cashiers -> listOf(("Number of language" to employeeClass.numberLanguages.toString()) to {
                (newEmployeeClass as EmployeeClass.Cashiers).numberLanguages = it.toInt()
            })

            is EmployeeClass.Dispatchers -> listOf(("Number of language" to employeeClass.numberLanguages.toString()) to {
                (newEmployeeClass as EmployeeClass.Cashiers).numberLanguages = it.toInt()
            })

            is EmployeeClass.Other -> listOf(("Type worker" to employeeClass.typeWorker) to {
                (newEmployeeClass as EmployeeClass.Other).typeWorker = it
            })

            is EmployeeClass.Pilots -> listOf()
            is EmployeeClass.Security -> listOf()
            is EmployeeClass.Techniques -> listOf()
        }
    }


    private fun generateUpdatePickField(
        employeeClass: EmployeeClass, newEmployeeClass: EmployeeClass
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(Triple("Employee" to employeeClass.employeeEntity!!.human!!.name + " " + employeeClass.employeeEntity!!.human!!.surname,
            { newEmployeeClass.employee = it.customGetId() },
            suspend { model.getEmployees() } to { (it as Employee).human!!.name + " " + it.human!!.surname })
        )
    }

    private fun generateUpdatePickFieldFromList(
        employeeClass: EmployeeClass, newEmployeeClass: EmployeeClass
    ): List<Triple<Pair<String, String>, (String) -> Unit, List<String>>> {
        return when (employeeClass) {
            is EmployeeClass.Administrator -> listOf()
            is EmployeeClass.Cashiers -> listOf()
            is EmployeeClass.Dispatchers -> listOf()
            is EmployeeClass.Other -> listOf()
            is EmployeeClass.Pilots -> {
                listOf(
                    Triple(
                        "Rating" to employeeClass.rating,
                        { (newEmployeeClass as EmployeeClass.Pilots).rating = it },
                        listOf("Instrument Rating", "Multi-Engine Rating")
                    ),
                    Triple(
                        "License category" to employeeClass.licenseCategory,
                        { (newEmployeeClass as EmployeeClass.Pilots).licenseCategory = it },
                        listOf(
                            "Airline Transport Pilot",
                            "Commercial Pilot",
                            "Senior Private Pilot",
                            "Private Pilot"
                        )
                    ),
                )
            }

            is EmployeeClass.Security -> {
                listOf(
                    Triple(
                        "Weapons permit" to if (employeeClass.weaponsPermission) "Yes" else "No", {
                            (newEmployeeClass as EmployeeClass.Security).weaponsPermission =
                                it == "Yes"
                        }, listOf("Yes", "No")
                    ),
                    Triple(
                        "Military service" to if (employeeClass.militaryService) "Yes" else "No", {
                            (newEmployeeClass as EmployeeClass.Security).militaryService =
                                it == "Yes"
                        }, listOf("Yes", "No")
                    ),
                )
            }

            is EmployeeClass.Techniques -> {
                listOf(
                    Triple(
                        "Qualification" to employeeClass.qualification,
                        { (newEmployeeClass as EmployeeClass.Techniques).qualification = it },
                        listOf("engines", "electronics", "refill", "wheels")
                    )
                )
            }
        }
    }

    private fun createDialog(employeeClass: EmployeeClass) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog_App
        ).setItems(items) { dialog, which ->
            when (which) {
                0 -> model.delete(employeeClass)
                1 -> openBottomDialogUpdate(employeeClass)
            }
            dialog.dismiss()
        }.show()
    }

    private fun openSideDialog() {
        SideSheetDialog(requireContext()).apply {
            val dialog = SideDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)

        }.show()
    }

    private fun openBottomDialogUpdate(employeeClass: EmployeeClass) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEmployeeClass = employeeClass.myCopy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(employeeClass, newEmployeeClass), requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(employeeClass, newEmployeeClass),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickFieldFromList(
                generateUpdatePickFieldFromList(
                    employeeClass, newEmployeeClass
                ), requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newEmployeeClass)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert(newEmployeeClass: EmployeeClass) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newEmployeeClass), requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newEmployeeClass), lifecycleScope, requireContext()
            )
            dialog.bottomLayout.addInsertPickFieldFromList(
                generateInitPickFieldFromList(newEmployeeClass), requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newEmployeeClass)
                dismiss()
            }
        }.show()
    }


    companion object {

        fun newInstance() = EmployeeClassFragment()
    }
}