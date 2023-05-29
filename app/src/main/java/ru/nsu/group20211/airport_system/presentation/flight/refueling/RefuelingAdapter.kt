package ru.nsu.group20211.airport_system.presentation.flight.refueling

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.Refueling
import ru.nsu.group20211.airportsystem.databinding.ListItemRefuelingBinding

class RefuelingAdapter(var list: List<Refueling>, var clickListener: (Refueling) -> Unit) :
    RecyclerView.Adapter<RefuelingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemRefuelingBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemRefuelingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Refueling) {
            with(binding) {
                this.refueling.text = ("Type fuel: ") + (data.tupeFule?.name ?: "Unknown")
                this.dateOfRefueling.text = "Date: " + (data.date?.toString() ?: "Unknown")
                this.brigade.text = "Brigade: " + (data.refuelingBrigade?.nameBrigade ?: "Unknown")
                this.refuelingLitters.text = "Liters: " + data.refilledLiters.toString()
                this.flightName.text = "Flight: " + (data.schedule?.getSchedule() ?: "Unknown")
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}