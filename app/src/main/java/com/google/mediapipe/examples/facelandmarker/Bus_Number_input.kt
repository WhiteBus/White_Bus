package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class Bus_Number_input : AppCompatActivity() {
    private lateinit var busNumberEditText: EditText
    private lateinit var busNumber: String
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_number_input)

        // Initialize the EditText
        busNumberEditText = findViewById(R.id.sample_EditText)




        val uid = auth.currentUser?.uid ?: return

        // You can add a listener to get the text when needed, for example on a button click
        // Assuming there is a button with id 'submit_button' in your layout (not shown in your provided XML)
        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            busNumber = busNumberEditText.text.toString()
            addBusNumberFieldToDriver(uid, busNumber)
            if(busNumber != null){
                val intent = Intent(this@Bus_Number_input, MainActivity::class.java)
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
}
