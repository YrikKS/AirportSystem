package ru.nsu.group20211.airport_system.presentation.flight.approximate_flight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airportsystem.databinding.ListItemApproximateFlightBinding

class ApproximateFlightAdapter(
    var list: List<ApproximateFlight>,
    var clickListener: (ApproximateFlight) -> Unit
) :
    RecyclerView.Adapter<ApproximateFlightAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemApproximateFlightBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemApproximateFlightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ApproximateFlight) {
            with(binding) {
                this.departureAirport.text = "Departure: " + data.departureAirport!!.airportName
                this.arrivalAirport.text = "Arrival: " + data.arrivalAirport!!.airportName
                this.price.text = "+-Price: " + data.approximatePrice.toString()
                this.frequencyInDays.text = "Frequency: " + data.frequencyInDays.toString()
                this.dateOfTakeOff.text = "Time: " + data.approximateTakeoffTime!!.toString()
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}