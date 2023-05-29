package ru.nsu.group20211.airport_system.presentation.flight.salon_maintenance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.SalonMaintenance
import ru.nsu.group20211.airportsystem.databinding.ListItemSalonMaintenanceBinding

class SalonMaintenanceAdapter(
    var list: List<SalonMaintenance>,
    var clickListener: (SalonMaintenance) -> Unit
) :
    RecyclerView.Adapter<SalonMaintenanceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemSalonMaintenanceBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemSalonMaintenanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SalonMaintenance) {
            with(binding) {
                this.dateOfRefueling.text = "Date: " + (data.date?.toString() ?: "Unknown")
                this.brigade.text = "Brigade: " + (data.brigade?.nameBrigade ?: "Unknown")
                this.flightName.text = "Flight: " + (data.schedule?.getSchedule() ?: "Unknown")
                this.report.text = "Report: " + data.report
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}