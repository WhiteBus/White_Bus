package com.google.mediapipe.examples.facelandmarker.favorite

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.Main_vi_Search_des
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.repository.DestinationRepository

class DestinationViewHolder(itemView: View, private val itemClickListener: (String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val destinationRepository: DestinationRepository
    val textViewName: TextView = itemView.findViewById(R.id.destinationName)
    val textViewAddress: TextView = itemView.findViewById(R.id.destinationAddress)

    init {
        val context = itemView.context
        destinationRepository = DestinationRepository(context) // Context를 사용하여 초기화

        itemView.setOnClickListener {
            val destinationName = textViewName.text.toString()
            Toast.makeText(context, "Clicked on: $destinationName", Toast.LENGTH_SHORT).show()
            // 여기다 즐겨찾기 검색 후 과정 추가
            val coor = destinationRepository.getDestinationCoordinatesByName(destinationName)
            switchActivity(context, Main_vi_Search_des::class.java)
        }
    }

    private fun switchActivity(context: Context, activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }
}
