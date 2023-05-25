package ru.nsu.group20211.airport_system.presentation.employee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.models.Employee
import ru.nsu.group20211.airportsystem.databinding.ListItemEmployeeBinding

class EmployeeAdapter(var list: List<Employee>, var cliclListner: (Employee) -> Unit) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemEmployeeBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Employee) {
            with(binding) {
                employeeSalary.text = "Salary: " + data.salary.toString()
                employeeBrigades.text = "Brigade: " + data.brigade?.nameBrigade
                dateOfEmployment.text = "Date of employment: " + data.dateOfEmployment?.toString()
                nameHuman.text =
                    "Worker: " + data.human!!.name + " " + data.human!!.surname + " " + (data.human?.patronymic ?: "")
                root.setOnClickListener {
                    cliclListner(data)
                }
            }
        }
    }
}