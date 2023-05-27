package ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.plane.models.LargeTechnicalInspection
import ru.nsu.group20211.airportsystem.databinding.ListItemLargeTechnicalInspectionBinding

class LargeTechnicalInspectionAdapter(
    var list: List<LargeTechnicalInspection>,
    var clickListener: (LargeTechnicalInspection) -> Unit
) : RecyclerView.Adapter<LargeTechnicalInspectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemLargeTechnicalInspectionBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemLargeTechnicalInspectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: LargeTechnicalInspection) {
            with(binding) {
                this.planeModel.text =
                    "Plane: " + data.planeEntity?.modelPlane?.nameModel + " " + data.planeEntity?.id
                this.repairBrigade.text =
                    "Repair brigade: " + (data.brigade?.nameBrigade ?: "Unknown")
                this.dateOfRepair.text = data.date.toString()
                this.report.text = data.resultInspection
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}