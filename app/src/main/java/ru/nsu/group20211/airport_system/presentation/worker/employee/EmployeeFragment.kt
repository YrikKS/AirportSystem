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
import ru.nsu.group20211.airport_system.addPickParamInternet
import ru.nsu.group20211.airport_system.addPickParamNoInternet
import ru.nsu.group20211.airport_system.addSlider
import ru.nsu.group20211.airport_system.addUpdateButton
import ru.nsu.group20211.airport_system.addUpdatePickField
import ru.nsu.group20211.airport_system.addUpdateTextField
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentEmployeeBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                binding.countElements.text = "count: $res"
            }
        }

        model.stateProvider.onEach {
            adapter.list = it
            if (it.isNotEmpty()) binding.countElements.text = "count: ${it.size}"
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

    private fun generateInitTextField(employee: Employee): List<Pair<String, (String) -> Unit>> {
        return listOf("Salary" to { employee.salary = it.toFloatOrNull() ?: 0.0F },
            "Date of employment (yyyy-mm-dd)" to {
                employee.dateOfEmployment = try {
                    Date.valueOf(it)
                } catch (ex: Exception) {
                    null
                }
            })
    }

    private fun generateInitPickField(employee: Employee): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(Triple("Human",
            { employee.humanId = it.customGetId() },
            suspend { model.getHumans() } to {
                (it as Human).getFIO()
            }), Triple("Brigade",
            { employee.idBrigade = it.customGetId() },
            suspend { model.getBrigades() } to { (it as Brigade).nameBrigade })
        )
    }

    private fun generateUpdateTextField(
        employee: Employee, newEmployee: Employee
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(("Salary" to employee.salary.toString()) to {
            newEmployee.salary = it.toFloatOrNull() ?: 0.0F
        },
            ("Date of employment (yyyy-mm-dd)" to employee.dateOfEmployment.toString()) to {
                newEmployee.dateOfEmployment = Date.valueOf(it)
            })
    }

    private fun generateUpdatePickField(
        employee: Employee, newEmployee: Employee
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(Triple("Human" to employee.human!!.getFIO(),
            { newEmployee.humanId = it.customGetId() },
            suspend { model.getHumans() } to { (it as Human).getFIO() }),
            Triple("Brigade" to employee.brigade!!.nameBrigade,
                { newEmployee.idBrigade = it.customGetId() },
                suspend { model.getBrigades() } to { (it as Brigade).nameBrigade })
        )
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Salary" to "salary",
            "Name" to "name",
            "Surname" to "surname",
            "Date of employment" to "dateOfEmployment",
        )
    }

    private fun generateSidePickParams(filter: DbFilter): List<Triple<String, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>, (DbEntity?) -> Unit>> {
        return listOf(
            Triple(
                "Brigade",
                suspend { model.getBrigades() } to { (it as Brigade).nameBrigade }
            ) {
                if (it != null) {
                    filter.queryMap[0] = """ "brigades"."id" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            },
            Triple("Department",
                suspend { model.getDepartment() } to { (it as Department).nameDepartment }) {
                if (it != null) {
                    filter.queryMap[0] = """ "departments"."id" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            }
        )
    }

    private fun generateSidePickParamNoInternet(filter: DbFilter): List<Triple<String, List<String>, (String?) -> Unit>> {
        return listOf(
            Triple(
                "Sex",
                listOf("Men", "Women")
            ) {
                if (it != null) {
                    filter.queryMap[3] = """ "human"."sex" = '${it.first()}' """
                } else {
                    filter.queryMap.remove(3)
                }
            }
        )
    }

    private fun generateSlide(filter: DbFilter): List<Triple<String, suspend () -> Pair<Float, Float>, (Float, Float) -> Unit>> {
        return listOf(
            Triple("Salary", suspend { model.getMinSalary() to model.getMaxSalary() }, { min, max ->
                filter.queryMap[1] = """ "salary" BETWEEN '$min' AND '$max' """
            }),
            Triple(
                "Experience",
                suspend {
                    model.getMinExperience().toFloat() to model.getMaxExperience().toFloat()
                }
            ) { min, max ->
                filter.queryMap[2] =
                    """ EXTRACT (YEAR FROM SYSDATE) - EXTRACT (YEAR FROM "employees"."dateOfEmployment") BETWEEN '$min' AND '$max' """
            }
        )
    }

    private fun openSideDialog() {
        SideSheetDialog(requireContext()).apply {
            val dialog = SideDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val filter = DbFilter()

            // Sort
            val fieldName = generateSidePickSort()
            dialog.layoutExposed.isVisible = true
            dialog.textExposed.isVisible = true
            dialog.layoutExposed.hint = "Sort by"
            dialog.textExposed.setText("None")
            dialog.textExposed.setItems(fieldName.map { item -> item.first }.toTypedArray())
            dialog.textExposed.setOnItemClickListener { parent, view, position, id ->
                filter.nameFieldSort = fieldName[id.toInt()].second
            }
            dialog.materialCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                filter.desc = isChecked
            }
            //Slide
            dialog.paramsContainer.addSlider(
                requireContext(),
                generateSlide(filter),
                lifecycleScope
            )

            //PickWithInternet
            dialog.paramsContainer.addPickParamInternet(
                requireContext(),
                generateSidePickParams(filter),
                lifecycleScope
            )

            // PircNoInternet
            dialog.paramsContainer.addPickParamNoInternet(
                requireContext(),
                generateSidePickParamNoInternet(filter)
            )


            dialog.button.setOnClickListener {
                val (listCond, listOrder) = filter.generateQuery()
                model.getData(listCond, listOrder)
                dismiss()
            }
        }.show()
    }

    private fun createDialog(employee: Employee) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog_App
        ).setItems(items) { dialog, which ->
            when (which) {
                0 -> model.delete(employee)
                1 -> openBottomDialogUpdate(employee)
            }
            dialog.dismiss()
        }.show()
    }


    private fun openBottomDialogUpdate(employee: Employee) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEmployee = employee.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(employee, newEmployee), requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(employee, newEmployee), lifecycleScope, requireContext()
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
                generateInitTextField(newEmployee), requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newEmployee), lifecycleScope, requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newEmployee)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = EmployeeFragment()
    }
}