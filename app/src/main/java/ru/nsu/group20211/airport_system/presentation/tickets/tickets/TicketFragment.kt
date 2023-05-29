package ru.nsu.group20211.airport_system.presentation.tickets.tickets

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
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket
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

class TicketFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: TicketsViewModel
    private lateinit var binding: FragmentPlaneBinding
    private lateinit var adapter: TicketsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[TicketsViewModel::class.java]
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
        adapter = TicketsAdapter(emptyList()) { createDialog(it) }
        binding.recyclePlane.adapter = adapter
        binding.recyclePlane.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclePlane.addItemDecoration(SpaceItemDecorator(3))
        binding.title.text = "Tickets"
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

    private fun generateInitTextField(entity: Ticket): List<Pair<String, (String) -> Unit>> {
        return listOf(

        )
    }

    private fun generateInitPickField(entity: Ticket): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Passenger",
                { entity.passenger = it.customGetId() },
                suspend { model.getPassenger() } to
                        { (it as Passenger).getFIO() }),
        )
    }

    private fun generateUpdateTextField(
        entity: Ticket,
        newEntity: Ticket
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Registration time (yyyy-mm-dd hh:mm:ss)" to entity.registrationTime.toString()) to {
                newEntity.registrationTime = getTimestampFrom(it)
            },
        )
    }

    private fun generateUpdatePickField(
        entity: Ticket,
        newEntity: Ticket
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Passenger" to entity.passengerEntity!!.getFIO(),
                { newEntity.passenger = it.customGetId() },
                suspend { model.getPassenger() } to
                        { (it as Passenger).getFIO() }),
        )
    }

    private fun generateSidePickSort(): List<Pair<String, String>> {
        return listOf(
            "None" to "None",
            "Name" to "name",
            "Registration time" to "registrationTime",
        )
    }

    private fun generateSidePickParams(filter: DbFilter): List<Triple<String, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>, (DbEntity?) -> Unit>> {
        return listOf(
            Triple(
                "Flight",
                suspend { model.getSchedule() } to { (it as FlightSchedule).getSchedule() }
            ) {
                if (it != null) {
                    filter.queryMap[2] = """ "tickets"."idFlight" = '${it.customGetId()}' """
                } else {
                    filter.queryMap.remove(2)
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
                    filter.queryMap[0] = """ "passengers"."sex" = '${it.first()}' """
                } else {
                    filter.queryMap.remove(0)
                }
            },
            Triple(
                "Luggage",
                listOf("Yes", "No")
            ) {
                if (it != null) {
                    filter.queryMap[1] = """ "luggage" = '${it.first()}' """
                } else {
                    filter.queryMap.remove(1)
                }
            }
        )
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
                        this.fieldName.text = "Date"
                        seekBar.addOnChangeListener { rangeSlider, value, fromUser ->
                            val min = Date(rangeSlider.values.first().toLong()).toString()
                            val max = Date(rangeSlider.values.last().toLong()).toString()
                            filter.queryMap[0] =
                                """ "registrationTime" >= TO_TIMESTAMP('$min 00:00:00', 'YYYY-MM-DD HH24:MI:SS.FF') AND "registrationTime" <= TO_TIMESTAMP('$max 23:59:59', 'YYYY-MM-DD HH24:MI:SS.FF') """
                        }
                    }
                }.root
            )
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


    private fun createDialog(entity: Ticket) {
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

    private fun openBottomDialogUpdate(entity: Ticket) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newEntity = entity.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(entity, newEntity),
                requireContext()
            )
            val myView = DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.layoutExposed.isVisible = true
                it.layoutExposed.hint = "Place"
                it.textExposed.setText(entity.place.toString())
                it
            }
            listOf(
                "Luggage" to arrayOf("Yes", "No")
            ).forEach { text ->
                dialog.bottomLayout.addView(
                    DialogInflatorBinding.inflate(
                        LayoutInflater.from(requireContext()),
                    ).let {
                        it.layoutExposed.isVisible = true
                        it.layoutExposed.hint = text.first
                        it.textExposed.setItems(text.second)
                        it.textExposed.setText(if (entity.luggage == 'Y') "Yes" else "No")
                        it.textExposed.setOnItemClickListener { parent, view, position, id ->
                            if (id.toInt() == 0) {
                                newEntity.luggage = 'Y'
                            } else {
                                newEntity.luggage = 'N'
                            }
                        }
                        it.root
                    }
                )
            }
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(context),
                ).let {
                    it.layoutExposed.isVisible = true
                    it.layoutExposed.hint = "Flight"
                    it.textExposed.setText(entity.schedule!!.getSchedule())

                    lifecycleScope.launch(Dispatchers.IO) {
                        val listItems = model.getSchedule()
                        withContext(Dispatchers.Main) {
                            it.textExposed.setItems(listItems.map { it.getSchedule() }
                                .toTypedArray())

                            it.textExposed.setOnItemClickListener { parent, view, position, id ->
                                newEntity.idFlight = listItems[id.toInt()].customGetId()
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val listItems = model.getEmptyPlace(newEntity.idFlight)
                                    withContext(Dispatchers.Main) {
                                        myView.textExposed.setItems(listItems.map { it.toString() }
                                            .toTypedArray())
                                        myView.textExposed.setOnItemClickListener { parent, view, position, id ->
                                            newEntity.place = listItems[id.toInt()].toInt()
                                        }
                                    }
                                }
                                myView.textExposed
                            }
                        }
                    }
                    it.root
                }
            )
            dialog.bottomLayout.addView(myView.root)
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
            val newEntity = Ticket()
            dialog.bottomLayout.addInsertTextField(
                generateInitTextField(newEntity),
                requireContext()
            )
            val myView = DialogInflatorBinding.inflate(
                LayoutInflater.from(context),
            ).let {
                it.layoutExposed.isVisible = true
                it.layoutExposed.hint = "Place"
                it
            }
            listOf(
                "Luggage" to arrayOf("Yes", "No")
            ).forEach { text ->
                dialog.bottomLayout.addView(
                    DialogInflatorBinding.inflate(
                        LayoutInflater.from(requireContext()),
                    ).let {
                        it.layoutExposed.isVisible = true
                        it.layoutExposed.hint = text.first
                        it.textExposed.setItems(text.second)
                        it.textExposed.setOnItemClickListener { parent, view, position, id ->
                            if (id.toInt() == 0) {
                                newEntity.luggage = 'Y'
                            } else {
                                newEntity.luggage = 'N'
                            }
                        }
                        it.root
                    }
                )
            }
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(context),
                ).let {
                    it.layoutExposed.isVisible = true
                    it.layoutExposed.hint = "Flight"
                    lifecycleScope.launch(Dispatchers.IO) {
                        val listItems = model.getSchedule()
                        withContext(Dispatchers.Main) {
                            it.textExposed.setItems(listItems.map { it.getSchedule() }
                                .toTypedArray())

                            it.textExposed.setOnItemClickListener { parent, view, position, id ->
                                newEntity.idFlight = listItems[id.toInt()].customGetId()
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val listItems = model.getEmptyPlace(newEntity.idFlight)
                                    withContext(Dispatchers.Main) {
                                        myView.textExposed.setItems(listItems.map { it.toString() }
                                            .toTypedArray())
                                        myView.textExposed.setOnItemClickListener { parent, view, position, id ->
                                            newEntity.place = listItems[id.toInt()].toInt()
                                        }
                                    }
                                }
                                myView.textExposed
                            }
                        }
                    }
                    it.root
                }
            )
            dialog.bottomLayout.addView(myView.root)
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
        fun newInstance() = TicketFragment()
    }
}