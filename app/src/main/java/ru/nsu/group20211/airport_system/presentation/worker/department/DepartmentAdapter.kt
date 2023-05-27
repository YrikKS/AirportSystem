package ru.nsu.group20211.airport_system.presentation.worker.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airportsystem.databinding.ListItemDepartmentBinding

class DepartmentAdapter(var list: List<Department>, var clickListener: (Department) -> Unit) :
    RecyclerView.Adapter<DepartmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDepartmentBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemDepartmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Department) {
            with(binding) {
                nameDepartment.text = data.nameDepartment
                this.boss.text =
                    (data.administrator?.employeeEntity?.human?.getFIO() ?: "Unknown")
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}