package ru.nsu.group20211.airport_system.presentation.worker.human

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airportsystem.databinding.ListItemHumanBinding

class HumanAdapter(var list: List<Human>, var cliclListner: (Human) -> Unit) :
    RecyclerView.Adapter<HumanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemHumanBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemHumanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Human) {
            with(binding) {
                nameHuman.text = data.getFIO()
                dateOfBirth.text = "Date of birth: " + data.dateOfBirth
                sexPeople.text = if (data.sex == 'W') "Women" else "Men"
                countChildren.text = "Count children: " + data.countChildren.toString()
                root.setOnClickListener() {
                    cliclListner(data)
                }
            }
        }
    }
}