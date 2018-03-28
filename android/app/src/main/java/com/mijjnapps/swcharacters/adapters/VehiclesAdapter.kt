package com.mijjnapps.swcharacters.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mijjnapps.swcharacters.R
import com.mijjnapps.swcharacters.data.SwVehicle
import com.mijjnapps.swcharacters.utils.inflate
import kotlinx.android.synthetic.main.item_vehicle.view.*


class VehiclesAdapter : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {

    private val vehicles: ArrayList<SwVehicle> = ArrayList()

    override fun getItemCount(): Int {
        return vehicles.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView = v.vehicleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.item_vehicle))

    override fun onBindViewHolder(h: ViewHolder, position: Int) {
        val v = vehicles[position]
        h.name.text = v.name
    }

    fun updateData(newVehicles: List<SwVehicle>) {
        vehicles.clear()
        vehicles.addAll(newVehicles)
        notifyDataSetChanged()
    }
}