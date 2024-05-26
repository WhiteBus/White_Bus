package com.google.mediapipe.examples.facelandmarker.favorite

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.database.DestinationContract

class DestinationAdapter(
    private val destinations: Cursor,
    private val itemClickListener: (String, String) -> Unit
) : RecyclerView.Adapter<DestinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destination, parent, false)
        return DestinationViewHolder(itemView, itemClickListener.toString())
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        destinations.moveToPosition(position)
        val name = destinations.getString(destinations.getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_NAME))
        val address = destinations.getString(destinations.getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_ADDRESS))
        holder.textViewName.text = name
        holder.textViewAddress.text = address
    }

    override fun getItemCount(): Int {
        return destinations.count
    }
}