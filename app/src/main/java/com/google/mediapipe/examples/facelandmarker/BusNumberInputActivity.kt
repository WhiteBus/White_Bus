package com.google.mediapipe.examples.facelandmarker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityAppUserDeterminantBinding
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityBusNumberInputBinding

class BusNumberInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusNumberInputBinding

    // Input Bus Number
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusNumberInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListener()
    }

    // When Button is clicked
    private fun clickListener() {
        binding.saveBusNumberBtn.setOnClickListener {
            val busNumber = binding.sampleEt.toString()
            if (busNumber == "") {  // 버스 번호가 입력되지 않았을 때
                binding.busNumberErrorTv.text = "버스 번호를 입력해주세요"
                binding.busNumberErrorTv.visibility = View.VISIBLE
            } else {  // 정상으로 버스 번호가 입력됨
                binding.busNumberErrorTv.visibility = View.GONE
                switchActivity(FaceSettingActivity())
            }
        }
    }

    // Switch the Activity function
    private fun switchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }

}