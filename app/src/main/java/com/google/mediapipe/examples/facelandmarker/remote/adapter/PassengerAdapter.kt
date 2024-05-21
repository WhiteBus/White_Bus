package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.databinding.ItemPassengerInformationBinding
import com.google.mediapipe.examples.facelandmarker.model.Passenger
import com.squareup.picasso.Picasso

class PassengerAdapter(private val passengers: List<Passenger>) : RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>() {
    class PassengerViewHolder(private val binding: ItemPassengerInformationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(passenger: Passenger) {
            binding.passengerNameTv.text = passenger.nickname
            if (passenger.profileImageUrl.isNotEmpty()) {
                Picasso.get().load(passenger.profileImageUrl).into(binding.passengerProfileIv)
            } else {
                binding.passengerProfileIv.setImageResource(R.drawable.ic_profile) // Use a placeholder image
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val binding = ItemPassengerInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PassengerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        holder.bind(passengers[position])
    }

    override fun getItemCount(): Int {
        return passengers.size
    }
}