package com.google.mediapipe.examples.facelandmarker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityAppUserDeterminantBinding

class AppUserDeterminantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppUserDeterminantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppUserDeterminantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickListener()
    }

    // When Button is clicked
    private fun clickListener() {
        // Dedicated button for the visually impaired
        binding.visualImpairedButton.setOnClickListener {
            // Move *** Activity -> 나중에 변경
            // switchActivity();
        }

        // Dedicated button for the bus driver
        binding.busDriverButton.setOnClickListener {
            // Move Login Activity
            switchActivity(BusNumberInputActivity());
        }
    }

    // Switch the Activity function
    private fun switchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }
}