package com.example.pdm_projeto.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_projeto.R
import com.example.pdm_projeto.data.Vehicle // Use a classe do pacote data
class VehicleAdapter(
    private var vehicles: MutableList<Vehicle>,
    private val onSelectVehicle: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvVehicleName)
        val detailsTextView: TextView = view.findViewById(R.id.tvVehicleDetails)
        val selectButton: Button = view.findViewById(R.id.btnSelectVehicle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.nameTextView.text = vehicle.name
        holder.detailsTextView.text = vehicle.details

        holder.selectButton.setOnClickListener {
            onSelectVehicle(vehicle)
        }
    }

    override fun getItemCount(): Int = vehicles.size

    // Update the data in the adapter
    fun updateData(newVehicles: List<Vehicle>) {
        vehicles.clear()
        vehicles.addAll(newVehicles)
        notifyDataSetChanged()
    }
}
