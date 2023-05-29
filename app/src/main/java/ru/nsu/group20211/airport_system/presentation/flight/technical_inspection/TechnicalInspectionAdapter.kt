package ru.nsu.group20211.airport_system.presentation.flight.technical_inspection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.TechnicalInspection
import ru.nsu.group20211.airportsystem.databinding.ListItemLargeTechnicalInspectionBinding
import ru.nsu.group20211.airportsystem.databinding.ListItemSalonMaintenanceBinding
import ru.nsu.group20211.airportsystem.databinding.ListItemTechnicalInspectionBinding

class TechnicalInspectionAdapter(
    var list: List<TechnicalInspection>,
    var clickListener: (TechnicalInspection) -> Unit
) :
    RecyclerView.Adapter<TechnicalInspectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemTechnicalInspectionBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemTechnicalInspectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TechnicalInspection) {
            with(binding) {
                this.dateOfRefueling.text = "Date: " + (data.date?.toString() ?: "Unknown")
                this.brigade.text = "Brigade: " + (data.brigade?.nameBrigade ?: "Unknown")
                this.flightName.text = "Flight: " + (data.schedule?.getSchedule() ?: "Unknown")
                this.report.text = "Report: " + data.resultInspection
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}