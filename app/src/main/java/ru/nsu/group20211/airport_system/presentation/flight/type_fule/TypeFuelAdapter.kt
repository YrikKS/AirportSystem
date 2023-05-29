package ru.nsu.group20211.airport_system.presentation.flight.type_fule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel
import ru.nsu.group20211.airportsystem.databinding.ListItemTypeFuelBinding

class TypeFuelAdapter(var list: List<TypeFuel>, var clickListener: (TypeFuel) -> Unit) :
    RecyclerView.Adapter<TypeFuelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemTypeFuelBinding.inflate(
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

    inner class ViewHolder(private val binding: ListItemTypeFuelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TypeFuel) {
            with(binding) {
                nameFuel.text = data.name
                root.setOnClickListener {
                    clickListener(data)
                }
            }
        }
    }
}