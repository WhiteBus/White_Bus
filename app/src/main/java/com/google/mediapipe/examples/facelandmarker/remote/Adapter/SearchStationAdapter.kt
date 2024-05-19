package com.google.mediapipe.examples.facelandmarker.remote.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.Main_vi_Search_des
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationInfo

class SearchStationAdapter(private val itemClickListener: Main_vi_Search_des) : RecyclerView.Adapter<SearchStationAdapter.StationViewHolder>() {

    private var stationList: List<SearchStationInfo> = listOf() // 역 정보 리스트

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationList[position]

        // 역 이름과 위치 정보를 ViewHolder의 View에 바인딩
        holder.stationNameTextView.text = station.stationName
        val location = "위도: ${station.y}, 경도: ${station.x}"
        holder.locationTextView.text = location
        val stationId = "stationId: ${station.stationID}"
        holder.stationIdView.text = stationId
        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(station)
        }
    }

    override fun getItemCount(): Int {
        return stationList.size
    }

    fun setStationList(stationList: List<SearchStationInfo>) {
        this.stationList = stationList
        notifyDataSetChanged()
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationNameTextView: TextView = itemView.findViewById(R.id.stationNameTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val stationIdView: TextView = itemView.findViewById(R.id.stationId)
    }
}