package ru.nsu.group20211.airport_system.presentation.worker.brigades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airportsystem.databinding.ListItemBrigadeBinding

class BrigadeAdapter(var list: List<Brigade>, var clickListener: (Brigade) -> Unit) :
    RecyclerView.Adapter<BrigadeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBrigadeBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemBrigadeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Brigade) {
            with(binding) {
                this.brigadeName.text = "Name: " + data.nameBrigade.toString()
                this.brigadeCountPeople.text = "Count people: " + (data.countPeople ?: "Unknown")
                this.avgSalary.text = "AVG salary: " + (data.avgSalary ?: "Unknown")
                this.sumSalary.text = "SUM salary: " + (data.sumSalary ?: "Unknown")
                this.departmentName.text =
                    "Department: " + (data.department?.nameDepartment ?: "Unknown")
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}