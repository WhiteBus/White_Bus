package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R

class UserAdapter(private val niclist: List<String>, private val imagelist: List<String>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.passenger_profile_iv)
        val nicknameTextView: TextView = itemView.findViewById(R.id.passenger_name_tv)
        val pushbell: ImageView = itemView.findViewById(R.id.pushed_bell_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_passenger_information, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.nicknameTextView.text = niclist[position]
        loadImageFromUrl(holder.profileImageView, imagelist[position])
    }

    override fun getItemCount(): Int {
        return niclist.size
    }

    private fun loadImageFromUrl(imageView: ImageView, url: String) {
        Thread {
            try {
                val inputStream = java.net.URL(url).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                imageView.post {
                    imageView.setImageResource(R.drawable.ic_profile)
                }
            }
        }.start()
    }
}
