package ru.nsu.group20211.airport_system.presentation.worker.brigades

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nsu.group20211.airport_system.addInsertButton
import ru.nsu.group20211.airport_system.addInsertPickField
import ru.nsu.group20211.airport_system.addInsertTextField
import ru.nsu.group20211.airport_system.addUpdateButton
import ru.nsu.group20211.airport_system.addUpdatePickField
import ru.nsu.group20211.airport_system.addUpdateTextField
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentBrigadeBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentEmployeeBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import javax.inject.Inject

class BrigadeFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: BrigadeViewModel
    private lateinit var binding: FragmentBrigadeBinding
    private lateinit var adapter: BrigadeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[BrigadeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrigadeBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }


    private fun setupUi() {
        adapter = BrigadeAdapter(emptyList()) { createDialog(it) }
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
                binding.title.text = "Brigades    count: $res"
            }
        }

        model.stateProvider
            .onEach {
                adapter.list = it
                if (it.isNotEmpty())
                    binding.title.text = "Brigades    count: ${it.size}"
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

    private fun generateInitTextField(brigade: Brigade): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "name" to { brigade.nameBrigade = it },
        )
    }

    private fun generateInitPickField(brigade: Brigade): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple("Depatment",
                { brigade.idDepartment = it.customGetId() },
                suspend { model.getDepartment() } to { (it as Department).nameDepartment })
        )
    }

    private fun generateUpdateTextField(
        brigade: Brigade,
        newBrigade: Brigade
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("name" to brigade.nameBrigade) to { newBrigade.nameBrigade = it },
        )
    }

    private fun generateUpdatePickField(
        brigade: Brigade,
        newBrigade: Brigade
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(("Depatment" to brigade.department!!.nameDepartment),
                { newBrigade.idDepartment = it.customGetId() },
                suspend { model.getDepartment() } to { (it as Department).nameDepartment })
        )
    }

    private fun createDialog(brigade: Brigade) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(brigade)
                    1 -> openBottomDialogUpdate(brigade)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun openSideDialog() {
        SideSheetDialog(requireContext()).apply {
            val dialog = SideDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
//            var salaryFilter = Pair("0", "${Int.MAX_VALUE}")
//            dialog.paramsContainer.addSlider(
//                "Salary",
//                requireContext(),
//                lifecycleScope,
//                suspend { model.getMinSalary() to model.getMaxSalary() },
//                { salaryFilter = salaryFilter.copy(first = it) },
//                { salaryFilter = salaryFilter.copy(second = it) }
//            )
//            var experienceFilter = Pair("0", "${Int.MAX_VALUE}")
//            dialog.paramsContainer.addSlider(
//                "Experience",
//                requireContext(),
//                lifecycleScope,
//                suspend {
//                    model.getMinExperience().toFloat() to model.getMaxExperience().toFloat()
//                },
//                { experienceFilter = experienceFilter.copy(first = it) },
//                { experienceFilter = experienceFilter.copy(second = it) }
//            )
//            var sexFilter = Pair(true, true)
//            dialog.paramsContainer.addView(
//                SideDialogParametrsInflatorBinding.inflate(LayoutInflater.from(requireContext()))
//                    .let {
//                        it.fieldName.isVisible = true
//                        it.fieldName.text = "Sex"
//                        it.checkBoxContainer.isVisible = true
//                        it.checkBoxContainer.addView(
//                            MaterialCheckBox(
//                                requireContext()
//                            ).apply {
//                                text = "Men"
//                                checkedState = MaterialCheckBox.STATE_CHECKED
//                                addOnCheckedStateChangedListener { checkBox, state ->
//                                    if (state == MaterialCheckBox.STATE_CHECKED) {
//                                        sexFilter = sexFilter.copy(first = false)
//                                    } else {
//                                        sexFilter = sexFilter.copy(first = true)
//                                    }
//                                }
//                            }
//                        )
//                        it.checkBoxContainer.addView(
//                            MaterialCheckBox(
//                                requireContext()
//                            ).apply {
//                                text = "Women"
//                                checkedState = MaterialCheckBox.STATE_CHECKED
//                                addOnCheckedStateChangedListener { checkBox, state ->
//                                    if (state == MaterialCheckBox.STATE_CHECKED) {
//                                        sexFilter = sexFilter.copy(second = false)
//                                    } else {
//                                        sexFilter = sexFilter.copy(second = true)
//                                    }
//                                }
//                            }
//                        )
//                        it.root
//                    }
//            )
//            dialog.button.setOnClickListener {
//                model.getData(
//                    listCond = listOf(
//                        """ ${Employee.getTableName()}."salary" BETWEEN ${salaryFilter.first} AND ${salaryFilter.second}  """,
//                        """ EXTRACT (YEAR FROM SYSDATE) - EXTRACT (YEAR FROM "employees"."dateOfEmployment") BETWEEN ${experienceFilter.first} AND ${experienceFilter.second}  """,
//                        buildString {
//                            append("")
//                            if (!sexFilter.first && !sexFilter.second) return@buildString
//                            append("(")
//                            append(if (sexFilter.first) """ ${Human.getTableName()}."sex" = 'M' """ else "")
//                            if (sexFilter.first && sexFilter.second) append("OR")
//                            append(if (sexFilter.second) """ ${Human.getTableName()}."sex" = 'W' """ else "")
//                            append(")")
//                        }
//
//
//                    )
//                )
//                dismiss()
//            }
        }.show()
    }

    private fun openBottomDialogUpdate(brigade: Brigade) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newBrigade = brigade.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(brigade, newBrigade),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(brigade, newBrigade),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newBrigade)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newBrigade = Brigade()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newBrigade),
                requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newBrigade),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newBrigade)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = BrigadeFragment()
    }
}