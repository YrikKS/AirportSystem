package ru.nsu.group20211.airport_system.presentation.flight.refueling

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
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.Refueling
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel
import ru.nsu.group20211.airport_system.getTimestampFrom
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentPlaneBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import javax.inject.Inject

class RefuelingFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: RefuelingViewModel
    private lateinit var binding: FragmentPlaneBinding
    private lateinit var adapter: RefuelingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[RefuelingViewModel::class.java]
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
        adapter = RefuelingAdapter(emptyList()) { createDialog(it) }
        binding.recyclePlane.adapter = adapter
        binding.recyclePlane.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclePlane.addItemDecoration(SpaceItemDecorator(3))
        binding.title.text = "Refueling"
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

    private fun generateInitTextField(refueling: Refueling): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Date refueling (yyyy-mm-dd hh:mm:ss)" to { refueling.date = getTimestampFrom(it) },
            "Liters" to {
                refueling.refilledLiters = it.toFloatOrNull() ?: let { 0F }
            },
        )
    }

    private fun generateInitPickField(refueling: Refueling): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Flight",
                { refueling.idFlight = it.customGetId() },
                suspend { model.getSchedule() } to
                        { (it as FlightSchedule).getSchedule() }),
            Triple(
                "Brigade",
                { refueling.idRefuelingTeam = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
            Triple(
                "Type fuel",
                { refueling.typeFuel = it.customGetId() },
                suspend { model.getTypeFuels() } to
                        { (it as TypeFuel).name }),
        )
    }

    private fun generateUpdateTextField(
        refueling: Refueling,
        newRefueling: Refueling
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Date refueling (yyyy-mm-dd hh:mm:ss)" to refueling.date.toString()) to {
                newRefueling.date = getTimestampFrom(it)
            },
            ("Liters" to refueling.refilledLiters.toString()) to {
                newRefueling.refilledLiters = it.toFloatOrNull() ?: let { 0F }
            },
        )

    }

    private fun generateUpdatePickField(
        refueling: Refueling,
        newRefueling: Refueling
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Flight" to (refueling.schedule?.getSchedule() ?: ""),
                { newRefueling.idFlight = it.customGetId() },
                suspend { model.getSchedule() } to
                        { (it as FlightSchedule).getSchedule() }),
            Triple(
                "Brigade" to (refueling.refuelingBrigade?.nameBrigade ?: ""),
                { newRefueling.idRefuelingTeam = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
            Triple(
                "Type fuel" to (refueling.tupeFule?.name ?: ""),
                { newRefueling.typeFuel = it.customGetId() },
                suspend { model.getTypeFuels() } to
                        { (it as TypeFuel).name }),
        )
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Type fuel" to """typeFule"."name""",
            "Refilled liters" to "refilledLiters",
            "Refueling team" to "nameBrigade",
            "Date" to "date",
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
            dialog.materialCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                filter.desc = isChecked
            }
            dialog.layoutExposed.isVisible = true
            dialog.textExposed.isVisible = true
            dialog.layoutExposed.hint = "Sort by"
            dialog.textExposed.setText("None")
            dialog.textExposed.setItems(fieldName.map { item -> item.first }.toTypedArray())
            dialog.textExposed.setOnItemClickListener { parent, view, position, id ->
                filter.nameFieldSort = fieldName[id.toInt()].second
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


    private fun createDialog(refueling: Refueling) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(refueling)
                    1 -> openBottomDialogUpdate(refueling)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun openBottomDialogUpdate(refueling: Refueling) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newRefueling = refueling.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(refueling, newRefueling),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(refueling, newRefueling),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newRefueling)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newRefueling = Refueling()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newRefueling),
                requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newRefueling),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newRefueling)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = RefuelingFragment()
    }
}