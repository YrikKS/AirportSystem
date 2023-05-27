package ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report

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
import ru.nsu.group20211.airport_system.addInsertTextField
import ru.nsu.group20211.airport_system.addUpdateButton
import ru.nsu.group20211.airport_system.addUpdatePickField
import ru.nsu.group20211.airport_system.addUpdateTextField
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.getTimestampFrom
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentPlaneBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import java.sql.Timestamp
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
                        { (it as Brigade).nameBrigade}),
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
                        { (it as Brigade).nameBrigade}),

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

    private fun openSideDialog() {
        SideSheetDialog(requireContext()).apply {
            val dialog = SideDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
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