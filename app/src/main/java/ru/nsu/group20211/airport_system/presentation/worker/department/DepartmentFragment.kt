package ru.nsu.group20211.airport_system.presentation.worker.department

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentDepartmentBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import javax.inject.Inject


class DepartmentFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: DepartmentViewModel
    private lateinit var binding: FragmentDepartmentBinding
    private lateinit var adapter: DepartmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[DepartmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepartmentBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }


    private fun setupUi() {
        adapter = DepartmentAdapter(emptyList()) { createDialog(it) }
        binding.recycleEmployee.adapter = adapter
        binding.recycleEmployee.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleEmployee.addItemDecoration(SpaceItemDecorator(3))
        binding.floatingEmployee.setOnClickListener {
            openBottomDialogInsert()
        }
        binding.buttonSide.setOnClickListener {
            openSideDialog()
        }

        model.stateProvider
            .onEach {
                adapter.list = it
                binding.countElements.text = "count: ${it.size}"
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

    private fun generateInitTextField(department: Department): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Name" to { department.nameDepartment = it },
        )
    }

    private fun generateInitPickField(department: Department): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Boss",
                { department.idBoss = it.customGetId() },
                suspend { model.getAdministrators() } to {
                    (it as EmployeeClass.Administrator).employeeEntity?.human?.getFIO() ?: ""
                }),
        )
    }

    private fun generateUpdateTextField(
        department: Department,
        newDepartment: Department
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Name" to department.nameDepartment) to { newDepartment.nameDepartment = it },
        )
    }

    private fun generateUpdatePickField(
        department: Department,
        newDepartment: Department
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Boss" to (department.administrator?.employeeEntity?.human?.getFIO() ?: ""),
                { newDepartment.idBoss = it.customGetId() },
                suspend { model.getAdministrators() } to {
                    (it as EmployeeClass.Administrator).employeeEntity?.human?.getFIO() ?: ""
                }),
        )
    }

    private fun createDialog(department: Department) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(department)
                    1 -> openBottomDialogUpdate(department)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Name Department" to "nameDepartment",
        )
    }

    private fun generateSidePickParams(filter: DbFilter): List<Triple<String, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>, (DbEntity?) -> Unit>> {
        return listOf()
    }

    private fun generateSidePickParamNoInternet(filter: DbFilter): List<Triple<String, List<String>, (String?) -> Unit>> {
        return listOf()
    }

    private fun generateSlide(filter: DbFilter): List<Triple<String, suspend () -> Pair<Float, Float>, (Float, Float) -> Unit>> {
        return listOf()
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

    private fun openBottomDialogUpdate(department: Department) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newDepartment = department.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(department, newDepartment),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(department, newDepartment),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newDepartment)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newDepartment = Department()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newDepartment),
                requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newDepartment),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newDepartment)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = DepartmentFragment()
    }
}