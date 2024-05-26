package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "RIdeBus"

class user_dropin_bus : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dropin_bus)
        auth = FirebaseAuth.getInstance()

        val uid = auth.uid


        val removebtn = findViewById<Button>(R.id.dropbtn)
        var busNumber: String = "" //버스 번호판 네자리 가져오기
        var nickname:String = ""
        var profileImageUrl:String = ""
        var stationid: String = intent.getStringExtra("stationid") ?: ""  // 인텐트에서 stationid 값을 가져옵니다.


        val muserid = auth.currentUser?.uid ?: return
        val muserDocRef = db.collection("BlindUser").document(muserid)
        muserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        nickname = (it["nickname"] as? String).toString()
                        profileImageUrl = (it["profileImageUrl"] as? String).toString()
                        busNumber = (it["pickupNUm"] as? String).toString()
                        //stationid = (it["stationId"] as? String).toString()

                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }





        removebtn.setOnClickListener{
            if (nickname != null && profileImageUrl != null) {
                if (uid != null) {
                    removeBlindToBus(busNumber)
                }
            }
            val intent = Intent(this@user_dropin_bus, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun removeBlindToBus(busnumber: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid

            db.collection("OnBus").document(busnumber).collection("blindUser").document(uid)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
}