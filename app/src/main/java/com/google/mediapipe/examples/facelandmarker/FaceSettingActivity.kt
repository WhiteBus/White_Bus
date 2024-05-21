package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityFaceSettingBinding

class FaceSettingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityFaceSettingBinding

    // Input Bus Number
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickListener();
    }

    private fun clickListener() {
        binding.startDriveCardView.setOnClickListener {
            // switchActivity() // 맵 뷰로 가야지~!!
        }
    }
}