package ru.nsu.group20211.airport_system.presentation.plane.plane

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airportsystem.databinding.ListItemPlaneBinding

class PlaneAdapter(var list: List<Plane>, var clickListener: (Plane) -> Unit) :
    RecyclerView.Adapter<PlaneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemPlaneBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: ListItemPlaneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Plane) {
            with(binding) {
                this.namePlane.text = "Name " + data.modelPlane?.nameModel + " " + data.id
                countFlight.text = "Count flight: " + data.countFlight
                this.numberPassengerSeats.text =
                    "Number passenger seats " + data.numberPassengerSeats.toString()
                this.dateOfCreation.text = "Date creation " + data.dateCreation.toString()
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}