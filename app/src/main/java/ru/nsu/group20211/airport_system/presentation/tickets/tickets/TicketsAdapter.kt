package ru.nsu.group20211.airport_system.presentation.tickets.tickets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket
import ru.nsu.group20211.airportsystem.databinding.ListItemTicketBinding

class TicketsAdapter(var list: List<Ticket>, var clickListener: (Ticket) -> Unit) :
    RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemTicketBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Ticket) {
            with(binding) {
                nameHuman.text = data.passengerEntity!!.getFIO()
                flight.text =
                    data.schedule!!.arrival!!.city + " " + data.schedule!!.arrival!!.airportName
                place.text = "Place: " + data.place
                this.luggage.text = "Luggage: " + if (data.luggage == 'Y') "Yes" else "No"
                this.price.text = "Price: " + data.realPrice
                this.registart.text = "Registration time: " + data.registrationTime.toString()
                root.setOnClickListener() {
                    clickListener(data)
                }
            }
        }
    }
}