package ru.nsu.group20211.airport_system.presentation.worker.employee_class

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airportsystem.databinding.ListItemEmployeeClassBinding

class EmployeeClassAdapter(
    var list: List<EmployeeClass>,
    var clickListener: (EmployeeClass) -> Unit
) : RecyclerView.Adapter<EmployeeClassAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemEmployeeClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: ListItemEmployeeClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: EmployeeClass) {
            with(binding) {
                nameHuman.text =
                    (data.employeeEntity?.human?.name ?: "Unknown") + " " +
                            (data.employeeEntity?.human?.surname ?: "Unknown") + " " +
                            (data.employeeEntity?.human?.patronymic ?: "")

                dateOfBirth.text =
                    "Date of birth: " + (data.employeeEntity?.human?.dateOfBirth ?: "Unknown")

                sexPeople.text = if (data.employeeEntity?.human?.sex == 'W') "Women" else "Men"

                countChildren.text =
                    "Count children: " + (data.employeeEntity?.human?.countChildren ?: "Unknown")

                when (data) {
                    is EmployeeClass.Administrator -> {
                        typeWorker.text = "Administrator"
                        employeeAttr1.isVisible = false
                        employeeAttr2.isVisible = false
                    }

                    is EmployeeClass.Cashiers -> {
                        typeWorker.text = "Cashiers"
                        employeeAttr1.text = "Number of languages: " + data.numberLanguages
                        employeeAttr1.isVisible = true
                        employeeAttr2.isVisible = false
                    }

                    is EmployeeClass.Dispatchers -> {
                        typeWorker.text = "Dispatchers"
                        employeeAttr1.text = "Number of languages: " + data.numberLanguages
                        employeeAttr1.isVisible = true
                        employeeAttr2.isVisible = false
                    }

                    is EmployeeClass.Other -> {
                        typeWorker.text = "Others employees"
                        employeeAttr1.text = "Type worker: " + data.typeWorker
                        employeeAttr1.isVisible = true
                        employeeAttr2.isVisible = false
                    }

                    is EmployeeClass.Pilots -> {
                        typeWorker.text = "Pilots"
                        employeeAttr1.text = "Rating: " + data.rating
                        employeeAttr2.text = "LicenseCategory: " + data.licenseCategory
                        employeeAttr1.isVisible = true
                        employeeAttr2.isVisible = true
                    }

                    is EmployeeClass.Security -> {
                        typeWorker.text = "Security service"
                        employeeAttr1.text = "Military service: " + data.militaryService
                        employeeAttr2.text = "Weapons permission: " + data.weaponsPermission
                        employeeAttr1.isVisible = true
                        employeeAttr2.isVisible = true
                    }

                    is EmployeeClass.Techniques -> {
                        typeWorker.text = "Techniques"
                        employeeAttr1.text = "Qualification: " + data.qualification
                        employeeAttr1.isVisible = true
                        employeeAttr2.isVisible = false
                    }
                }
                employeeAttr1
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}