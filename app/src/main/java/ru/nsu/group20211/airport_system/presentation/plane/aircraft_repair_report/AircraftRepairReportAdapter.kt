package ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport
import ru.nsu.group20211.airportsystem.databinding.ListItemAircraftRepairReportBinding

class AircraftRepairReportAdapter(
    var list: List<AircraftRepairReport>,
    var clickListener: (AircraftRepairReport) -> Unit
) : RecyclerView.Adapter<AircraftRepairReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemAircraftRepairReportBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemAircraftRepairReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AircraftRepairReport) {
            with(binding) {
                this.planeModel.text =
                    "Plane: " + data.planeEntity?.modelPlane?.nameModel + " " + data.planeEntity?.id
                this.repairBrigade.text =
                    "Repair brigade: " + (data.brigade?.nameBrigade ?: "Unknown")
                this.dateOfRepair.text = data.dateRepair.toString()
                this.report.text = data.report
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}