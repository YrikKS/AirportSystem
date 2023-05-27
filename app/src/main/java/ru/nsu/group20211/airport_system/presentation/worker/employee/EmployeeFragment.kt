package ru.nsu.group20211.airport_system.presentation.worker.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nsu.group20211.airport_system.addInsertButton
import ru.nsu.group20211.airport_system.addInsertPickField
import ru.nsu.group20211.airport_system.addInsertTextField
import ru.nsu.group20211.airport_system.addSlider
import ru.nsu.group20211.airport_system.addUpdateButton
import ru.nsu.group20211.airport_system.addUpdatePickField
import ru.nsu.group20211.airport_system.addUpdateTextField
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentEmployeeBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogParametrsInflatorBinding
import java.sql.Date
import javax.inject.Inject

class EmployeeFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: EmployeeViewModel
    private lateinit var binding: FragmentEmployeeBinding
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[EmployeeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }


    private fun setupUi() {
        adapter = EmployeeAdapter(emptyList()) { createDialog(it) }
        binding.recycleEmployee.adapter = adapter
        binding.recycleEmployee.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleEmployee.addItemDecoration(SpaceItemDecorator(3))
        binding.floatingEmployee.setOnClickListener {
            openBottomDialogInsert()
        }
        binding.buttonSide.setOnClickListener {
            openSideDialog()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val res = model.getCount()
            withContext(Dispatchers.Main) {
                binding.title.text = "Employees    count: $res"
            }
        }

        model.stateProvider
            .onEach {
                adapter.list = it
                if (it.isNotEmpty())
                    binding.title.text = "Employees    count: ${it.size}"
                adapter.notifyDataSetChanged()
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)


        model.errorProvider
            .onEach {
                it.printStackTrace()
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    it.message ?: "",
                    Snackbar.LENGTH_SHORT
                ).setAction("Refresh") {
                    model.getData()
                }.show()
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        model.getData()
    }

    private fun generateInitTextField(employee: Employee): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Salary" to { employee.salary = it.toFloatOrNull() ?: 0.0F },
            "Date of employment" to {
                employee.dateOfEmployment = try {
                    Date.valueOf(it)
                } catch (ex: Exception) {
                    null
                }
            }
        )
    }

    private fun generateInitPickField(employee: Employee): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Human",
                { employee.humanId = it.customGetId() },
                suspend { model.getHumans() } to {
                    (it as Human).name + " " + it.surname + " " + (it.patronymic ?: "")
                }),
            Triple(
                "Brigade",
                { employee.idBrigade = it.customGetId() },
                suspend { model.getBrigades() } to { (it as Brigade).nameBrigade }
            )
        )
    }

    private fun generateUpdateTextField(
        employee: Employee,
        newEmployee: Employee
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Salary" to employee.salary.toString())
                    to { newEmployee.salary = it.toFloatOrNull() ?: 0.0F },
            ("Date of employment" to employee.dateOfEmployment.toString()) to
                    { newEmployee.dateOfEmployment = Date.valueOf(it) }
        )
    }

    private fun generateUpdatePickField(
        employee: Employee,
        newEmployee: Employee
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Human" to employee.human!!.name + " " + employee.human!!.surname + " " + employee.human!!.patronymic + " ",
                { newEmployee.humanId = it.customGetId() },
                suspend { model.getHumans() } to { (it as Human).name + " " + it.surname + " " + it.patronymic }),
            Triple(
                "Brigade" to employee.brigade!!.nameBrigade,
                { newEmployee.idBrigade = it.customGetId() },
                suspend { model.getBrigades() } to { (it as Brigade).nameBrigade }
            )
        )
    }

    private fun createDialog(employee: Employee) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(employee)
                    1 -> openBottomDialogUpdate(employee)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun openSideDialog() {
        SideSheetDialog(requireContext()).apply {
            val dialog = SideDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            var salaryFilter = Pair("0", "${Int.MAX_VALUE}")
            dialog.paramsContainer.addSlider(
                "Salary",
                requireContext(),
                lifecycleScope,
                suspend { model.getMinSalary() to model.getMaxSalary() },
                { salaryFilter = salaryFilter.copy(first = it) },
                { salaryFilter = salaryFilter.copy(second = it) }
            )
            var experienceFilter = Pair("0", "${Int.MAX_VALUE}")
            dialog.paramsContainer.addSlider(
                "Experience",
                requireContext(),
                lifecycleScope,
                suspend {
                    model.getMinExperience().toFloat() to model.getMaxExperience().toFloat()
                },
                { experienceFilter = experienceFilter.copy(first = it) },
                { experienceFilter = experienceFilter.copy(second = it) }
            )
            var sexFilter = Pair(true, true)
            dialog.paramsContainer.addView(
                SideDialogParametrsInflatorBinding.inflate(LayoutInflater.from(requireContext()))
                    .let {
                        it.fieldName.isVisible = true
                        it.fieldName.text = "Sex"
                        it.checkBoxContainer.isVisible = true
                        it.checkBoxContainer.addView(
                            MaterialCheckBox(
                                requireContext()
                            ).apply {
                                text = "Men"
                                checkedState = MaterialCheckBox.STATE_CHECKED
                                addOnCheckedStateChangedListener { checkBox, state ->
                                    if (state == MaterialCheckBox.STATE_CHECKED) {
                                        sexFilter = sexFilter.copy(first = false)
                                    } else {
                                        sexFilter = sexFilter.copy(first = true)
                                    }
                                }
                            }
                        )
                        it.checkBoxContainer.addView(
                            MaterialCheckBox(
                                requireContext()
                            ).apply {
                                text = "Women"
                                checkedState = MaterialCheckBox.STATE_CHECKED
                                addOnCheckedStateChangedListener { checkBox, state ->
                                    if (state == MaterialCheckBox.STATE_CHECKED) {
                                        sexFilter = sexFilter.copy(second = false)
                                    } else {
                                        sexFilter = sexFilter.copy(second = true)
                                    }
                                }
                            }
                        )
                        it.root
                    }
            )
            dialog.button.setOnClickListener {
                model.getData(
                    listCond = listOf(
                        """ ${Employee.getTableName()}."salary" BETWEEN ${salaryFilter.first} AND ${salaryFilter.second}  """,
                        """ EXTRACT (YEAR FROM SYSDATE) - EXTRACT (YEAR FROM "employees"."dateOfEmployment") BETWEEN ${experienceFilter.first} AND ${experienceFilter.second}  """,
                        buildString {
                            append("")
                            if (!sexFilter.first && !sexFilter.second) return@buildString
                            append("(")
                            append(if (sexFilter.first) """ ${Human.getTableName()}."sex" = 'M' """ else "")
                            if (sexFilter.first && sexFilter.second) append("OR")
                            append(if (sexFilter.second) """ ${Human.getTableName()}."sex" = 'W' """ else "")
                            append(")")
                        }


                    )
                )
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogUpdate(employee: Employee) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEmployee = employee.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(employee, newEmployee),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(employee, newEmployee),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newEmployee)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEmployee = Employee()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newEmployee),
                requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newEmployee),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newEmployee)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() =
            EmployeeFragment()
    }
}