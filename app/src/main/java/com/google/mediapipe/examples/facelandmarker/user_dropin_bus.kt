package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.Main_Bus_Arrival.Companion.busTransitCount

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
        var dropin: String = ""


        val muserid = auth.currentUser?.uid ?: return
        val muserDocRef = db.collection("BlindUser").document(muserid)
        muserDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        nickname = (it["nickname"] as? String).toString()
                        profileImageUrl = (it["profileImageUrl"] as? String).toString()
                        busNumber = (it["pickupNum"] as? String).toString()
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
            updateDropInStatus(busNumber, uid.toString())
            //removeBlindToBus(busNumber)
            // busTransitCount 감소시키기
            busTransitCount--
            // 조건에 따라 액티비티 이동
            val intent = if (busTransitCount > 0) {
                Intent(this@user_dropin_bus, Main_Bus_Arrival::class.java)
            } else {
                Intent(this@user_dropin_bus, MainActivity::class.java)
            }
            startActivity(intent)
        }
    }

    private fun updateDropInStatus(busnumber: String, uid: String) {
        // dropin? 컬럼을 0으로 설정
        val dropinUpdate = mapOf("dropin?" to 1)
        db.collection("OnBus").document(busnumber).collection("blindUser").document(uid)
            .update(dropinUpdate)
            .addOnSuccessListener {
                Log.d(TAG, "Drop-in status updated to 0")

                // 1분 후 dropin? 컬럼을 제거하는 핸들러 설정
                Handler(Looper.getMainLooper()).postDelayed({
                    removeDropInStatus(busnumber, uid)
                }, 60000) // 60000ms = 1 minute
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating drop-in status", e)
            }
    }

    private fun removeDropInStatus(busnumber: String, uid: String) {
        val dropinRemove = mapOf("dropin?" to FieldValue.delete())
        db.collection("OnBus").document(busnumber).collection("blindUser").document(uid)
            .update(dropinRemove)
            .addOnSuccessListener {
                Log.d(TAG, "Drop-in status removed")
                removeBlindToBus(busnumber)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error removing drop-in status", e)
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