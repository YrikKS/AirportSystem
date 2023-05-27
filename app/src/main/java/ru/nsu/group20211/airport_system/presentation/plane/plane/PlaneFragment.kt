package ru.nsu.group20211.airport_system.presentation.plane.plane

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
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.getTimeFrom
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentPlaneBinding
import ru.nsu.group20211.airportsystem.databinding.SideDialogBinding
import javax.inject.Inject

class PlaneFragment : Fragment() {
    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var model: PlaneViewModel
    private lateinit var binding: FragmentPlaneBinding
    private lateinit var adapter: PlaneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[PlaneViewModel::class.java]
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
        adapter = PlaneAdapter(emptyList()) { createDialog(it) }
        binding.recyclePlane.adapter = adapter
        binding.recyclePlane.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclePlane.addItemDecoration(SpaceItemDecorator(3))
        binding.title.text = "Planes"
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

    private fun generateInitTextField(plane: Plane): List<Pair<String, (String) -> Unit>> {
        return listOf(
            "Date creation (yyyy-mm-dd hh:mm:ss)" to { plane.dateCreation = getTimeFrom(it) },
            "Number of passenger seats" to {
                plane.numberPassengerSeats = it.toIntOrNull() ?: let { 0 }
            },
        )
    }

    private fun generateInitPickField(plane: Plane): List<Triple<String, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Model",
                { plane.model = it.customGetId() },
                suspend { model.getModelPlane() } to
                        { (it as ModelPlane).nameModel }),
        )
    }

    private fun generateUpdateTextField(
        plane: Plane,
        newPlane: Plane
    ): List<Pair<Pair<String, String>, (String) -> Unit>> {
        return listOf(
            ("Date creation (yyyy-mm-dd hh:mm:ss)" to plane.dateCreation.toString()) to {
                newPlane.dateCreation = getTimeFrom(it)
            },
            ("Number of passenger seats" to plane.numberPassengerSeats.toString()) to {
                newPlane.numberPassengerSeats = it.toIntOrNull() ?: let { 0 }
            },
        )
    }

    private fun generateUpdatePickField(
        plane: Plane,
        newPlane: Plane
    ): List<Triple<Pair<String, String>, (DbEntity) -> Unit, Pair<suspend () -> List<DbEntity>, (DbEntity) -> String>>> {
        return listOf(
            Triple(
                "Model" to plane.modelPlane!!.nameModel,
                { newPlane.model = it.customGetId() },
                suspend { model.getModelPlane() } to
                        { (it as ModelPlane).nameModel }),
        )
    }

    private fun createDialog(plane: Plane) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(plane)
                    1 -> openBottomDialogUpdate(plane)
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

    private fun openBottomDialogUpdate(plane: Plane) {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newPlane = plane.copy()
            dialog.bottomLayout.addUpdateTextField(
                generateUpdateTextField(plane, newPlane),
                requireContext()
            )
            dialog.bottomLayout.addUpdatePickField(
                generateUpdatePickField(plane, newPlane),
                lifecycleScope,
                requireContext()
            )
            dialog.bottomLayout.addUpdateButton(requireContext()) {
                model.update(newPlane)
                dismiss()
            }
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            val dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val newReport = Plane()
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
        fun newInstance() = PlaneFragment()
    }
}