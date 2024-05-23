package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.Main_vi_Search_des
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationInfo

class SearchStationAdapter(
    private val itemClickListener: (SearchStationInfo) -> Unit
) : RecyclerView.Adapter<SearchStationAdapter.StationViewHolder>() {

    private val stationList = mutableListOf<SearchStationInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stationList[position], itemClickListener)
    }

    override fun getItemCount(): Int = stationList.size

    fun setStationList(stations: List<SearchStationInfo>) {
        stationList.clear()
        stationList.addAll(stations)
        notifyDataSetChanged()
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stationName: TextView = itemView.findViewById(R.id.stationNameTextView)

        fun bind(station: SearchStationInfo, clickListener: (SearchStationInfo) -> Unit) {
            stationName.text = station.stationName
            itemView.setOnClickListener { clickListener(station) }
        }
    }
}