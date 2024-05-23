package com.google.mediapipe.examples.facelandmarker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityBusNumberInputBinding

class BusNumberInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusNumberInputBinding
    private lateinit var busNumberEditText: EditText
    private lateinit var busNumber: String
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Input Bus Number
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusNumberInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the EditText
        busNumberEditText = findViewById(R.id.sample_et)

        val uid = auth.currentUser?.uid ?: return

        // Button click listener
        clickListener(uid)
    }

    private fun clickListener(uid: String) {
        binding.saveBusNumberBtn.setOnClickListener {
            busNumber = binding.sampleEt.text.toString()
            if (busNumber.isEmpty()) {  // 버스 번호가 입력되지 않았을 때
                binding.busNumberErrorTv.text = "버스 번호를 입력해주세요"
                binding.busNumberErrorTv.visibility = View.VISIBLE
            } else {  // 정상으로 버스 번호가 입력됨
                binding.busNumberErrorTv.visibility = View.GONE
                addBusNumberFieldToDriver(uid, busNumber)
                switchActivity(FaceSettingActivity())
            }
        }
    }

    private fun addBusNumberFieldToDriver(driverUid: String, busNumberValue: String) {
        val driverDocRef = db.collection("DriverUser").document(driverUid)

        // update() 메서드를 사용하여 새로운 필드 추가
        driverDocRef.update("busNumb", busNumberValue)
            .addOnSuccessListener {
                println("busNumb field added successfully to Driver")
            }
            .addOnFailureListener { e ->
                println("Error adding busNumb field to Driver: $e")
            }
    }

    // Switch the Activity function
    private fun switchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }
}
