package com.google.mediapipe.examples.facelandmarker

import android.app.Activity
import android.content.Intent
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
            switchActivity(DriverMainActivity());
        }
    }

    // Switch the Activity function
    private fun switchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }
}