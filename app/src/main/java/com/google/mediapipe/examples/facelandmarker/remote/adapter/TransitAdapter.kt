package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.dto.TransitInfo

class TransitAdapter(private val transitList: List<TransitInfo>) : RecyclerView.Adapter<TransitAdapter.TransitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bus_path, parent, false)
        return TransitViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransitViewHolder, position: Int) {
        val transitInfo = transitList[position]
        holder.transitInfo.text = transitInfo.busNo
        holder.transitStationStartName.text = transitInfo.startName
        holder.transitStationEndName.text = transitInfo.endName
        holder.sectionDistance.text = "${(transitInfo.distance)/1000} m"
        holder.sectionTime.text = "${transitInfo.sectionTime} min"
    }

    override fun getItemCount() = transitList.size

    class TransitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transitInfo: TextView = view.findViewById(R.id.transit_info)
        val transitStationStartName: TextView = view.findViewById(R.id.transit_info_startName)
        val transitStationEndName: TextView = view.findViewById(R.id.transit_info_endName)
        val sectionDistance: TextView = view.findViewById(R.id.section_distance)
        val sectionTime: TextView = view.findViewById(R.id.section_time)
    }
}