package ru.nsu.group20211.airport_system.presentation.tickets.passengers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airportsystem.databinding.ListItemHumanBinding

class PassengerAdapter(var list: List<Passenger>, var clickListener: (Passenger) -> Unit) :
    RecyclerView.Adapter<PassengerAdapter.ViewHolder>() {

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

        fun bind(data: Passenger) {
            with(binding) {
                nameHuman.text = data.getFIO()
                dateOfBirth.text = "Date of birth: " + data.dateOfBirth
                sexPeople.text = if (data.sex == 'W') "Women" else "Men"

                countChildren.isVisible = false
                root.setOnClickListener() {
                    clickListener(data)
                }
            }
        }
    }
}