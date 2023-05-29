package ru.nsu.group20211.airport_system.presentation.flight.flight_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airportsystem.databinding.ListItemFlightScheduleBinding

class FlightScheduleAdapter(
    var list: List<FlightSchedule>,
    var clickListener: (FlightSchedule) -> Unit
) :
    RecyclerView.Adapter<FlightScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemFlightScheduleBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemFlightScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FlightSchedule) {
            with(binding) {
                this.scheduleInfo.text =
                    data.departure!!.city + "->" + data.arrival!!.city + " " + data.takeoffTime
                this.departureAirport.text =
                    "Departure: " + data.departure!!.city + " " + data.departure!!.airportName
                this.arrivalAirport.text =
                    "Arrival: " + data.arrival!!.city + " " + data.arrival!!.airportName
                this.minNumberTickets.text = "Min tickets: " + data.minNumberTickets
                this.price.text = "Price: " + data.price
                this.plane.text =
                    "Plane: " + data.planeEntity!!.modelPlane!!.nameModel + " " + data.plane
                this.brigadePilot.text = "Pilots: " + data.pilots!!.nameBrigade
                this.status.text = "Status: " + FlightScheduleFragment.status[data.status]
                this.type.text = "Type: " + FlightScheduleFragment.typeFlight[data.typeFlight]
                this.nowNumberTickets.text = "Need buy: " + data.noNeedTickets
                this.porcentNumberTickets.text = data.procentNoNeedTickets.toString() + "%"
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}