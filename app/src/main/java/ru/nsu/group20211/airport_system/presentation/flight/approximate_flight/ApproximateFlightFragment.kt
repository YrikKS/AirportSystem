package ru.nsu.group20211.airport_system.presentation.flight.approximate_flight

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
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airport_system.getTimestampFrom
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentPlaneBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import javax.inject.Inject

class ApproximateFlightFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: ApproximateFlightViewModel
    private lateinit var binding: FragmentPlaneBinding
    private lateinit var adapter: ApproximateFlightAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[ApproximateFlightViewModel::class.java]
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
        adapter = ApproximateFlightAdapter(emptyList()) { createDialog(it) }
        binding.recyclePlane.adapter = adapter
        binding.recyclePlane.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclePlane.addItemDecoration(SpaceItemDecorator(3))
        binding.title.text = "Approximate flights"
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

    private fun generateInitTextField(approximateFlight: ApproximateFlight): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Frequency in days" to { approximateFlight.frequencyInDays = it.toIntOrNull() ?: 0 },
            "Approximate take off time (yyyy-mm-dd hh:mm:ss)" to {
                approximateFlight.approximateTakeoffTime = getTimestampFrom(it)
            },
            "Approximate price" to {
                approximateFlight.approximatePrice = it.toFloatOrNull() ?: let { 0F }
            },
        )
    }

    private fun generateInitPickField(approximateFlight: ApproximateFlight): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Departure airport",
                { approximateFlight.idDepartureAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).airportName }),
            Triple(
                "Arrival airport",
                { approximateFlight.idArrivalAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).airportName }),
        )
    }

    private fun generateUpdateTextField(
        approximateFlight: ApproximateFlight,
        newApproximateFlight: ApproximateFlight
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Frequency in days" to approximateFlight.frequencyInDays.toString()) to {
                newApproximateFlight.frequencyInDays = it.toIntOrNull() ?: 0
            },
            ("Approximate take off time (yyyy-mm-dd hh:mm:ss)" to approximateFlight.approximateTakeoffTime!!.toString()) to {
                newApproximateFlight.approximateTakeoffTime = getTimestampFrom(it)
            },
            ("Approximate price" to approximateFlight.approximatePrice.toString()) to {
                newApproximateFlight.approximatePrice = it.toFloatOrNull() ?: let { 0F }
            },
        )
    }

    private fun generateUpdatePickField(
        approximateFlight: ApproximateFlight,
        newApproximateFlight: ApproximateFlight
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Departure airport" to approximateFlight.departureAirport!!.airportName,
                { newApproximateFlight.idDepartureAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).airportName }),
            Triple(
                "Arrival airport" to approximateFlight.arrivalAirport!!.airportName,
                { newApproximateFlight.idArrivalAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).airportName }),
        )
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Arrival airport" to """arrival"."airportName""",
            "Departure airport" to """departure"."airportName""",
            "Frequency in days" to "frequencyInDays",
            "Approximate price" to "approximatePrice",
        )
    }

    private fun generateSidePickParams(filter: DbFilter): List<Triple<String, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>, (DbEntity?) -> Unit>> {
        return listOf(
            Triple(
                "Airport arrival",
                suspend { model.getAirports() } to { (it as Airport).city + " " + it.airportName }
            ) {
                if (it != null) {
                    filter.queryMap[0] = """ "arrival"."id" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            },
        )
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


    private fun createDialog(refueling: ApproximateFlight) {
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

    private fun openBottomDialogUpdate(approximateFlight: ApproximateFlight) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newApproximateFlight = approximateFlight.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(approximateFlight, newApproximateFlight),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(approximateFlight, newApproximateFlight),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newApproximateFlight)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newApproximateFlight = ApproximateFlight()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newApproximateFlight),
                requireContext()
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newApproximateFlight),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newApproximateFlight)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = ApproximateFlightFragment()
    }
}