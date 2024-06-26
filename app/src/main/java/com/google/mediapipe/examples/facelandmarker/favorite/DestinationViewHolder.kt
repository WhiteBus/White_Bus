package com.google.mediapipe.examples.facelandmarker.favorite

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.Main_Bus_Arrival
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.searchPubPathT
import com.google.mediapipe.examples.facelandmarker.repository.DestinationRepository

class DestinationViewHolder(itemView: View, private val itemClickListener: String) :
    RecyclerView.ViewHolder(itemView) {

    private val destinationRepository: DestinationRepository
    val textViewName: TextView = itemView.findViewById(R.id.destinationName)
    val textViewAddress: TextView = itemView.findViewById(R.id.destinationAddress)

    init {
        val context = itemView.context
        destinationRepository = DestinationRepository(context) // Context를 사용하여 초기화

        itemView.setOnClickListener {
            val destinationName = textViewName.text.toString()
            val address = textViewAddress.text.toString()
            Toast.makeText(context, "Clicked on: $destinationName", Toast.LENGTH_SHORT).show()

            // 즐겨찾기 검색 후 과정 추가
            val repository = DestinationRepository(context)
            val coordinates = repository.getDestinationCoordinatesByName(destinationName)
            repository.close()

            if (coordinates != null) {
                val (endX, endY) = coordinates
                // Intent를 통해 searchPubPathT 액티비티로 데이터 전달
                val intent = Intent(context, searchPubPathT::class.java)
                intent.putExtra("endStationX", endX)
                intent.putExtra("endStationY", endY)
                intent.putExtra("laststationname", address) // address 값을 전달
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No data found for: $destinationName", Toast.LENGTH_SHORT).show()
            }
//            // 햄버거 버튼에 담긴 최종목적지 이름 바로 보내기
//            val intentnew = Intent(context,Main_Bus_Arrival::class.java)
//            intentnew.putExtra("laststationname", destinationName) // 마지막 정류장 이름 추가
        }
    }
}