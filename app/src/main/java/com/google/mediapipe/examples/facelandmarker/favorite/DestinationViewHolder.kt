package com.google.mediapipe.examples.facelandmarker.favorite

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R

class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewName: TextView = itemView.findViewById(R.id.textViewName)
    val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
}
