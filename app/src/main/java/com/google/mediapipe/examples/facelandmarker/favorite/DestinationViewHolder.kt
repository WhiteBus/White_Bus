package com.google.mediapipe.examples.facelandmarker.favorite

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R

class DestinationViewHolder(itemView: View, private val itemClickListener: (String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    val textViewName: TextView = itemView.findViewById(R.id.destinationName)
    val textViewAddress: TextView = itemView.findViewById(R.id.destinationAddress)

    init {
        itemView.setOnClickListener {
            val context = itemView.context
            val destinationName = textViewName.text.toString()
            Toast.makeText(context, "Clicked on: $destinationName", Toast.LENGTH_SHORT).show()
//           여기다 즐겨찾기 검색 후 과정 추가
        }
    }
}