package ru.nsu.group20211.airport_system.presentation.plane.model_plane

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airportsystem.databinding.ListItemModelPlaneBinding

class ModelPlaneAdapter(var list: List<ModelPlane>, var clickListener: (ModelPlane) -> Unit) :
    RecyclerView.Adapter<ModelPlaneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemModelPlaneBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemModelPlaneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ModelPlane) {
            with(binding) {
                this.planeModel.text = "Model: " + data.nameModel
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}