package ru.nsu.group20211.airport_system.presentation.human

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.nsu.group20211.airport_system.appComponent
import ru.nsu.group20211.airport_system.domain.models.Human
import ru.nsu.group20211.airport_system.domain.models.human
import ru.nsu.group20211.airport_system.presentation.SpaceItemDecorator
import ru.nsu.group20211.airport_system.setItems
import ru.nsu.group20211.airportsystem.R
import ru.nsu.group20211.airportsystem.databinding.BottomDialogBinding
import ru.nsu.group20211.airportsystem.databinding.DialogInflatorBinding
import ru.nsu.group20211.airportsystem.databinding.FragmentHumanBinding
import java.sql.Date
import java.text.SimpleDateFormat
import javax.inject.Inject

class HumanFragment : Fragment() {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory
    private lateinit var model: HumanViewModel

    @Inject
    lateinit var router: Router

    private lateinit var binding: FragmentHumanBinding
    private lateinit var adapter: HumanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        model = ViewModelProvider(this, modelFactory)[HumanViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHumanBinding.inflate(inflater, container, false)
        setupUi()
        return binding.root
    }

    private fun setupUi() {
        adapter = HumanAdapter(emptyList()) { createDialog(it) }
        binding.recycleHuman.adapter = adapter
        binding.recycleHuman.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleHuman.addItemDecoration(SpaceItemDecorator(3))
        binding.floatingHuman.setOnClickListener {
            openBottomDialogInsert()
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

    private fun createDialog(human: Human) {
        val items = arrayOf("Delete", "Update")
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_App)
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> model.delete(human)
                    1 -> openBottomDialogUpdate(human)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun openBottomDialogUpdate(human: Human) {
        BottomSheetDialog(requireContext()).apply {
            var dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val mapElement = mutableMapOf<String, TextView>()
            listOf(
                "Name" to human.name,
                "Surname" to human.surname,
                "Patronymic" to human.patronymic,
                "Count children" to human.countChildren.toString(),
                "Date of birth" to human.dateOfBirth.toString()
            ).forEach { data ->
                dialog.bottomLayout.addView(
                    DialogInflatorBinding.inflate(
                        LayoutInflater.from(requireContext()),
                    ).let {
                        it.textInputLayout.isVisible = true
                        it.textInputLayout.hint = data.first
                        it.inputText.setText(data.second)
                        mapElement[data.first] = it.inputText
                        it.root
                    }
                )
            }
            listOf(
                Triple("Sex", arrayOf("M", "W"), human.sex)
            ).forEach { data ->
                dialog.bottomLayout.addView(
                    DialogInflatorBinding.inflate(
                        LayoutInflater.from(requireContext()),
                    ).let {
                        it.layoutExposed.isVisible = true
                        it.layoutExposed.hint = data.first
                        it.textExposed.setItems(data.second)
                        it.textExposed.setText(data.third.toString())
                        mapElement[data.first] = it.textExposed
                        it.root
                    }
                )
            }
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(requireContext()),
                ).let {
                    it.bottomButton.isVisible = true
                    it.bottomButton.text = "Update"
                    it.bottomButton.setOnClickListener {
                        val newHuman = human.copy()
                        dialog.bottomLayout.forEachIndexed { index, view ->
                            mapElement.forEach { key, view ->
                                when (key) {
                                    "Name" -> newHuman.name = view.text.toString()
                                    "Surname" -> newHuman.surname = view.text.toString()
                                    "Pyrtonymic" -> newHuman.patronymic = view.text.toString()
                                    "Count children" -> newHuman.countChildren =
                                        view.text.toString().toInt()

                                    "Date of birth" -> {
                                        newHuman.dateOfBirth = try {
                                            val format = "yyyy-MM-dd"
                                            val formater = SimpleDateFormat(format)
                                            Date(formater.parse(view.text.toString()).time)
                                        } catch (ex: Exception) {
                                            null
                                        } ?: try {
                                            val format = "dd.MM.yyyy"
                                            val formater = SimpleDateFormat(format)
                                            Date(formater.parse(view.text.toString()).time)
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            null
                                        }
                                    }

                                    "Sex" -> newHuman.sex = view.text.toString().first()
                                }
                            }
                        }
                        model.update(newHuman)
                        dismiss()
                    }
                    it.root
                }
            )
        }.show()
    }

    private fun openBottomDialogInsert() {
        BottomSheetDialog(requireContext()).apply {
            var dialog = BottomDialogBinding.inflate(LayoutInflater.from(requireContext()))
            setContentView(dialog.root)
            val mapElement = mutableMapOf<String, TextView>()
            listOf(
                "Name",
                "Surname",
                "Patronymic",
                "Count children",
                "Date of birth"
            ).forEach { text ->
                dialog.bottomLayout.addView(
                    DialogInflatorBinding.inflate(
                        LayoutInflater.from(requireContext()),
                    ).let {
                        it.textInputLayout.isVisible = true
                        it.textInputLayout.hint = text
                        mapElement[text] = it.inputText
                        it.root
                    }
                )
            }
            listOf(
                "Sex" to arrayOf("M", "W")
            ).forEach { text ->
                dialog.bottomLayout.addView(
                    DialogInflatorBinding.inflate(
                        LayoutInflater.from(requireContext()),
                    ).let {
                        it.layoutExposed.isVisible = true
                        it.layoutExposed.hint = text.first
                        it.textExposed.setItems(text.second)
                        mapElement[text.first] = it.textExposed
                        it.root
                    }
                )
            }
            dialog.bottomLayout.addView(
                DialogInflatorBinding.inflate(
                    LayoutInflater.from(requireContext()),
                ).let {
                    it.bottomButton.isVisible = true
                    it.bottomButton.text = "Insert"
                    it.bottomButton.setOnClickListener {
                        val newHuman = human {}
                        dialog.bottomLayout.forEachIndexed { index, view ->
                            mapElement.forEach { key, view ->
                                when (key) {
                                    "Name" -> newHuman.name = view.text.toString()
                                    "Surname" -> newHuman.surname = view.text.toString()
                                    "Patronymic" -> newHuman.patronymic = view.text.toString()
                                    "Count children" -> newHuman.countChildren =
                                        view.text.toString().toInt()

                                    "Date of birth" -> {
                                        newHuman.dateOfBirth = try {
                                            val format = "dd.MM.yyyy"
                                            val formater = SimpleDateFormat(format)
                                            Date(formater.parse(view.text.toString()).time)
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            null
                                        }
                                    }

                                    "Sex" -> newHuman.sex = view.text.toString().first()
                                }
                            }
                        }
                        model.insert(newHuman)
                        dismiss()
                    }
                    it.root
                }
            )
        }.show()
    }

    companion object {
        fun newInstance() =
            HumanFragment().apply {

            }
    }
}