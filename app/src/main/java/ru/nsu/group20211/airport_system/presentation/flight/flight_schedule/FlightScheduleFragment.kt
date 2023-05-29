package ru.nsu.group20211.airport_system.presentation.flight.flight_schedule

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
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.getTimestampFrom
import ru.nsu.group20211.airport_system.presentation.DbFilter
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.DialogInflatorBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentPlaneBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogParametrsInflatorBinding
import java.sql.Date
import javax.inject.Inject


class FlightScheduleFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: FlightScheduleViewModel
    private lateinit var binding: FragmentPlaneBinding
    private lateinit var adapter: FlightScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[FlightScheduleViewModel::class.java]
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
        adapter = FlightScheduleAdapter(emptyList()) { createDialog(it) }
        binding.recyclePlane.adapter = adapter
        binding.recyclePlane.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclePlane.addItemDecoration(SpaceItemDecorator(3))
        binding.title.text = "Flight schedule"
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

    private fun generateInitTextField(entity: FlightSchedule): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Take off time (yyyy-mm-dd hh:mm:ss)" to { entity.takeoffTime = getTimestampFrom(it) },
            "Boarding time (yyyy-mm-dd hh:mm:ss)" to { entity.boardingTime = getTimestampFrom(it) },
            "Price" to { entity.price = it.toFloatOrNull() ?: 0F },
            "Min number tickets" to { entity.minNumberTickets = it.toIntOrNull() ?: 0 },
        )
    }

    private fun generateInitPickField(entity: FlightSchedule): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Brigade pilots ",
                { entity.brigadePilots = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
            Triple(
                "Brigade worker",
                { entity.brigadeWorker = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
            Triple(
                "Plane",
                { entity.plane = it.customGetId() },
                suspend { model.getPlanes() } to
                        { (it as Plane).modelPlane!!.nameModel + " " + it.id }),
            Triple(
                "Approximate flight",
                { entity.idApproximateFlights = it.customGetId() },
                suspend { model.getApproximateFlight() } to
                        { (it as ApproximateFlight).arrivalAirport!!.city + " " + it.arrivalAirport!!.airportName + " " + it.approximateTakeoffTime }),
            Triple(
                "Arrival airport",
                { entity.idArrivalAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).city + " " + it.airportName }),
            Triple(
                "Departure airport",
                { entity.idDepartureAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).city + " " + it.airportName }),
        )
    }

    private fun generateUpdateTextField(
        entity: FlightSchedule,
        newEntity: FlightSchedule
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Take off time (yyyy-mm-dd hh:mm:ss)" to (entity.takeoffTime
                ?: "").toString()) to { newEntity.takeoffTime = getTimestampFrom(it) },
            ("Boarding time (yyyy-mm-dd hh:mm:ss)" to (entity.boardingTime
                ?: "").toString()) to {
                newEntity.boardingTime = getTimestampFrom(it)
            },

            ("Price" to entity.price.toString()) to { newEntity.price = it.toFloatOrNull() ?: 0F },
            ("Min number tickets" to entity.minNumberTickets.toString()) to {
                newEntity.minNumberTickets = it.toIntOrNull() ?: 0
            },
        )
    }

    private fun generateUpdatePickField(
        entity: FlightSchedule,
        newEntity: FlightSchedule
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Brigade pilots" to entity.pilots!!.nameBrigade,
                { newEntity.brigadePilots = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
            Triple(
                "Brigade worker" to entity.workers!!.nameBrigade,
                { newEntity.brigadeWorker = it.customGetId() },
                suspend { model.getBrigades() } to
                        { (it as Brigade).nameBrigade }),
            Triple(
                "Plane" to entity.planeEntity!!.modelPlane!!.nameModel + " " + entity.plane,
                { newEntity.plane = it.customGetId() },
                suspend { model.getPlanes() } to
                        { (it as Plane).modelPlane!!.nameModel + " " + it.id }),
            Triple(
                "Approximate flight" to entity.arrival!!.city + " " + entity.arrival!!.airportName + " " + entity.approximateFlight!!.approximateTakeoffTime,
                { newEntity.idApproximateFlights = it.customGetId() },
                suspend { model.getApproximateFlight() } to
                        { (it as ApproximateFlight).arrivalAirport!!.city + " " + it.arrivalAirport!!.airportName + " " + it.approximateTakeoffTime }),
            Triple(
                "Arrival airport" to entity.arrival!!.city + " " + entity.arrival!!.airportName,
                { newEntity.idArrivalAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).city + " " + it.airportName }),
            Triple(
                "Departure airport" to entity.departure!!.city + " " + entity.departure!!.airportName,
                { newEntity.idDepartureAirport = it.customGetId() },
                suspend { model.getAirports() } to
                        { (it as Airport).city + " " + it.airportName }),
        )
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Take off time" to "takeoffTime",
            "Boarding time" to "boardingTime",
            "Price" to "price",
            "Min number tickets" to "minNumberTickets",
            "Type flight" to """typesFlights"."title""",
            "Status flight" to """flightStatus"."flightStatus""",
            "Arrival city" to """arrival"."city""",
            "Departure city" to """departure"."city""",
            "Arrival airportName" to """arrival"."airportName""",
            "Departure airportName" to """departure"."airportName""",
            "Plane" to """modelPlane"."nameModel"""
        )
    }

    private fun generateSidePickParams(filter: DbFilter): List<Triple<String, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>, (DbEntity?) -> Unit>> {
        return listOf(
            Triple(
                "Arrival city",
                suspend { model.getAirports() } to { (it as Airport).city }
            ) {
                if (it != null) {
                    filter.queryMap[0] =
                        """ "flightSchedule"."idArrivalAirport" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            },
            Triple(
                "Arrival airport",
                suspend { model.getAirports() } to { (it as Airport).airportName }
            ) {
                if (it != null) {
                    filter.queryMap[0] =
                        """ "flightSchedule"."idArrivalAirport" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            },
            Triple(
                "Plane",
                suspend { model.getPlanes() } to { (it as Plane).modelPlane!!.nameModel + " " + it.id }
            ) {
                if (it != null) {
                    filter.queryMap[0] =
                        """ "flightSchedule"."plane" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            },
        )
    }

    private fun generateSidePickParamNoInternet(filter: DbFilter): List<Triple<String, List<String>, (String?) -> Unit>> {
        return listOf(
            Triple(
                "Status",
                status.values.toList()
            ) {
                if (it != null) {
                    filter.queryMap[3] =
                        """ "flightSchedule"."status" = '${getStatusFromString(it)}' """
                } else {
                    filter.queryMap.remove(3)
                }
            },
            Triple(
                "Type flight",
                typeFlight.values.toList()
            ) {
                if (it != null) {
                    filter.queryMap[3] =
                        """ "flightSchedule"."typeFlight" = '${getTypeFlightFromString(it)}' """
                } else {
                    filter.queryMap.remove(3)
                }
            }
        )
    }

    private fun generateSlide(filter: DbFilter): List<Triple<String, suspend () -> Pair<Float, Float>, (Float, Float) -> Unit>> {
        return listOf(
            Triple("Price", suspend { model.getMinPrice() to model.getMaxPrice() }) { min, max ->
                filter.queryMap[1] = """ "price" BETWEEN '$min' AND '$max' """
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
            dialog.paramsContainer.addView(
                SideDialogParametrsInflatorBinding.inflate(
                    LayoutInflater.from(context)
                ).also {
                    with(it) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val min = Date(model.getMinTakeOffTime().time)
                            val max = Date(model.getMaxTakeOffTime().time)
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
                        this.fieldName.text = "Take off time"
                        seekBar.addOnChangeListener { rangeSlider, value, fromUser ->
                            val min = Date(rangeSlider.values.first().toLong()).toString()
                            val max = Date(rangeSlider.values.last().toLong()).toString()
                            filter.queryMap[0] =
                                """ "flightSchedule"."takeoffTime" >= TO_TIMESTAMP('$min 00:00:00', 'YYYY-MM-DD HH24:MI:SS.FF') AND "flightSchedule"."takeoffTime" <= TO_TIMESTAMP('$max 23:59:59', 'YYYY-MM-DD HH24:MI:SS.FF') """
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


    private fun createDialog(entity: FlightSchedule) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(entity)
                    1 -> openBottomDialogUpdate(entity)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun openBottomDialogUpdate(entity: FlightSchedule) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEntity = entity.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(entity, newEntity),
                requireContext()
            )
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(context),
                ).let {
                    it.layoutExposed.isVisible = true
                    it.layoutExposed.hint = "Status"
                    it.textExposed.setText(status[entity.status])
                    it.textExposed.setItems(status.values.toTypedArray())
                    it.textExposed.setOnItemClickListener { parent, view, position, id ->
                        newEntity.status = id.toInt() + 1
                    }
                    it.root
                }
            )
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(context),
                ).let {
                    it.layoutExposed.isVisible = true
                    it.layoutExposed.hint = "Type flight"
                    it.textExposed.setText(typeFlight[entity.typeFlight])
                    it.textExposed.setItems(typeFlight.values.toTypedArray())
                    it.textExposed.setOnItemClickListener { parent, view, position, id ->
                        if (id.toInt() != 21) {
                            newEntity.typeFlight = id.toInt() + 1
                        } else {
                            newEntity.typeFlight = 21
                        }
                    }
                    it.root
                }
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(entity, newEntity),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newEntity)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEntity = FlightSchedule()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newEntity),
                requireContext()
            )
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(context),
                ).let {
                    it.layoutExposed.isVisible = true
                    it.layoutExposed.hint = "Status"
                    it.textExposed.setItems(status.values.toTypedArray())
                    it.textExposed.setOnItemClickListener { parent, view, position, id ->
                        newEntity.status = id.toInt() + 1
                    }
                    it.root
                }
            )
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(context),
                ).let {
                    it.layoutExposed.isVisible = true
                    it.layoutExposed.hint = "Type flight"
                    it.textExposed.setItems(typeFlight.values.toTypedArray())
                    it.textExposed.setOnItemClickListener { parent, view, position, id ->
                        if (id.toInt() != 21) {
                            newEntity.typeFlight = id.toInt() + 1
                        } else {
                            newEntity.typeFlight = 21
                        }
                    }
                    it.root
                }
            )
            dialog.bottomLayout.addInsertPickField(
                generateInitPickField(newEntity),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addInsertButton(context) {
                model.insert(newEntity)
                dismiss()
            }
        }.show()
    }

    companion object {
        fun newInstance() = FlightScheduleFragment()

        fun getStatusFromString(str: String): Int {
            return status.entries.findLast { it.value == str }?.key ?: 1
        }

        fun getTypeFlightFromString(str: String): Int {
            return typeFlight.entries.findLast { it.value == str }?.key ?: 1
        }

        val status: Map<Int, String> = mapOf(
            1 to "Взлёт",
            2 to "Регистрация",
            3 to "Задерживается",
            4 to "Ожидание",
            5 to "Отменён"
        )

        val typeFlight: Map<Int, String> = mapOf(
            1 to "Внтуренний",
            2 to "Международний",
            3 to "Чартерный",
            4 to "Грузовой",
            21 to "Специальные рейсы"
        )

    }
}