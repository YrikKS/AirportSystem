package ru.nsu.group20211.airport_system.presentation.flight.airport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airportsystem.databinding.ListItemAirportBinding

class AirportAdapter(var list: List<Airport>, var clickListener: (Airport) -> Unit) :
    RecyclerView.Adapter<AirportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemAirportBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemAirportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Airport) {
            with(binding) {
                nameCity.text = "City: " + data.city
                nameAirport.text = "Airport: " + data.airportName
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}