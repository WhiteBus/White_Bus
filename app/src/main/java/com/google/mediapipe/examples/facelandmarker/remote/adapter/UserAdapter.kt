package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.R

data class User(
    val profileImageUrl: String = "",
    val nickname: String = ""
)

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val nicknameTextView: TextView = itemView.findViewById(R.id.nicknameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nicknameTextView.text = user.nickname
        // 프로필 이미지 로드
        loadImageFromUrl(holder.profileImageView, user.profileImageUrl)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    private fun loadImageFromUrl(imageView: ImageView, url: String) {
        // URL에서 이미지를 비트맵으로 로드하는 작업을 비동기로 수행
        Thread {
            try {
                val inputStream = java.net.URL(url).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 오류 시 기본 이미지 설정
                imageView.post {
                    imageView.setImageResource(R.drawable.default_profile_image) // 기본 이미지
                }
            }
        }.start()
    }
}
