package ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report

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
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.getTimestampFrom
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentPlaneBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogParametrsInflatorBinding
import java.sql.Date
import javax.inject.Inject

class AircraftRepairReportFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: AircraftRepairReportViewModel
    private lateinit var binding: FragmentPlaneBinding
    private lateinit var adapter: AircraftRepairReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[AircraftRepairReportViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaneBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }


    private fun setupUi() {
        adapter = AircraftRepairReportAdapter(emptyList()) { createDialog(it) }
        binding.recyclePlane.adapter = adapter
        binding.recyclePlane.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclePlane.addItemDecoration(SpaceItemDecorator(3))
        binding.title.text = "Aircraft repair report"
        binding.floatingPlane.setOnClickListener {
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

    private fun generateInitTextField(report: AircraftRepairReport): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Full date (yyyy-mm-dd hh:mm:ss)" to { report.dateRepair = getTimestampFrom(it) },
            "Report" to { report.report = it },
        )
    }

    private fun generateInitPickField(report: AircraftRepairReport): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Plane",
                { report.plane = it.customGetId() },
                suspend { model.getPlanes() } to
                        { (it as Plane).modelPlane!!.nameModel + " " + it.id }),
            Triple(
                "Brigade",
                { report.repairTeam = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
        )
    }

    private fun generateUpdateTextField(
        report: AircraftRepairReport,
        newReport: AircraftRepairReport
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Full date (yyyy-mm-dd hh:mm:ss)" to report.dateRepair.toString()) to {
                newReport.dateRepair = getTimestampFrom(it)
            },
            ("Report" to report.report) to { newReport.report = it },
        )
    }

    private fun generateUpdatePickField(
        report: AircraftRepairReport,
        newReport: AircraftRepairReport
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Plane" to report.planeEntity?.modelPlane?.nameModel + " " + report.planeEntity?.id,
                { newReport.plane = it.customGetId() },
                suspend { model.getPlanes() } to
                        { (it as Plane).modelPlane!!.nameModel + " " + it.id }),
            Triple(
                "Brigade" to (report.brigade?.nameBrigade ?: "Unknown"),
                { newReport.repairTeam = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),

            )
    }

    private fun createDialog(report: AircraftRepairReport) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(report)
                    1 -> openBottomDialogUpdate(report)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Plane" to "plane",
            "Repair team" to "repairTeam",
            "Date repair" to "dateRepair",
            "Report" to "report",
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
            dialog.paramsContainer.addView(
                SideDialogParametrsInflatorBinding.inflate(
                    LayoutInflater.from(context)
                ).also {
                    with(it) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val min = Date(model.getMinDate().time)
                            val max = Date(model.getMaxDate().time)
                            withContext(Dispatchers.Main) {
                                seekBar.setValues(min.time.toFloat(), max.time.toFloat())
                                seekBar.isVisible = true
                                seekBar.valueFrom = min.time.toFloat()
                                seekBar.valueTo = max.time.toFloat()
                            }
                            seekBar.setLabelFormatter { value ->
                                Date(value.toLong()).toString()
                            }
                        }
                        this.fieldName.isVisible = true
                        this.fieldName.text = "Date"
                        seekBar.addOnChangeListener { rangeSlider, value, fromUser ->
                            val min = Date(rangeSlider.values.first().toLong()).toString()
                            val max = Date(rangeSlider.values.last().toLong()).toString()
                            filter.queryMap[0] =
                                """ "aircraftRepairReports"."dateRepair" >= TO_DATE('$min', 'YYYY-MM-DD') AND "aircraftRepairReports"."dateRepair" <= TO_DATE('$max', 'YYYY-MM-DD') """
                        }
                    }
                }.root
            )
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


    private fun openBottomDialogUpdate(report: AircraftRepairReport) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newReport = report.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(report, newReport),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(report, newReport),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newReport)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newReport = AircraftRepairReport()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newReport),
                requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newReport),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newReport)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = AircraftRepairReportFragment()
    }
}