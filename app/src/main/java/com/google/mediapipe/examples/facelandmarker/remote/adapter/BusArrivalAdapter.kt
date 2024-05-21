package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.dto.BusArrival

class BusArrivalAdapter(private val busArrivalList: List<BusArrival>) :
    RecyclerView.Adapter<BusArrivalAdapter.BusArrivalViewHolder>() {

    class BusArrivalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvArrivalBusNum: TextView = view.findViewById(R.id.tv_arrivalBusNum)
        val tvBusPlateNo: TextView = view.findViewById(R.id.tv_busPlateNo)
        val tvArrivalTime: TextView = view.findViewById(R.id.tv_arrivalTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusArrivalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bus_arrival, parent, false)
        return BusArrivalViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusArrivalViewHolder, position: Int) {
        val busArrival = busArrivalList[position]
        holder.tvArrivalBusNum.text = busArrival.routeNm

        // Null 체크 추가
        val arrival = busArrival.arrival1
        if (arrival != null) {
            holder.tvBusPlateNo.text = arrival.busPlateNo
            holder.tvArrivalTime.text = "${arrival.arrivalSec / 60} min ${arrival.arrivalSec % 60} sec"
        } else {
            holder.tvBusPlateNo.text = "N/A"
            holder.tvArrivalTime.text = "N/A"
        }
    }

    override fun getItemCount(): Int {
        return busArrivalList.size
    }
}