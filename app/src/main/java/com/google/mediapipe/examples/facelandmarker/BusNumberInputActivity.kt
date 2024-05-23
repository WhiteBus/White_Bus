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
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityAppUserDeterminantBinding
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
        setContentView(R.layout.activity_bus_number_input)

        // Initialize the EditText
        busNumberEditText = findViewById(R.id.sample_et)




        val uid = auth.currentUser?.uid ?: return

        // You can add a listener to get the text when needed, for example on a button click
        // Assuming there is a button with id 'submit_button' in your layout (not shown in your provided XML)
        val submitButton = findViewById<Button>(R.id.save_bus_number_btn)
        submitButton.setOnClickListener {
            busNumber = busNumberEditText.text.toString()
            addBusNumberFieldToDriver(uid, busNumber)
            if(busNumber != null){
                val intent = Intent(this@BusNumberInputActivity, FaceSettingActivity::class.java)
                startActivity(intent)
            }
        }
    }


    fun addBusNumberFieldToDriver(driverUid: String, busNumberValue: String) {
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