package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Wait_bus"

class OnStationUser : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var currentBusNo: String = "" // 전역 변수 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_user_popup)
        auth = FirebaseAuth.getInstance()

        val uid = auth.uid

        val staybtn = findViewById<Button>(R.id.btn)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        var nickname:String = ""
        var profileImageUrl:String = ""
        var stationid:String = "" //Main_NearestStation.GlobalValue_first.stationID //정류장 id가져오기
        var pickupnum: String =""
        var longitude: String= ""
        var latitude: String = ""

        // Intent로부터 데이터 가져오기
        // currentBusNo = intent.getStringExtra("busNo") ?: ""


        val userid = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("BlindUser").document(userid)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        nickname = (it["nickname"] as? String).toString()
                        profileImageUrl = (it["profileImageUrl"] as? String).toString()
                        pickupnum = (it["pickupNum"] as? String).toString()
                        longitude = (it["longitude"] as? String).toString()
                        latitude = (it["latitude"] as? String).toString()
                        stationid = (it["stationId"] as? String).toString()

                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
        staybtn.setOnClickListener{
            Log.w("HI", "abcdefg: ${nickname}, ${profileImageUrl}" )
            addBlindToStation(stationid, nickname, profileImageUrl, pickupnum, longitude, latitude)
            val intent = Intent(it.context, RideBus::class.java) // RideBus로 변경
            // Add station data to the intent
            intent.putExtra("busNo", currentBusNo) // 전역 변수 사용
        }

    }

    fun addBlindToStation(stationid: String, nickname: String, profileImageUrl: String, pickupnum: String, longitude: String, latitude: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl
            blind["pickupNum"] = pickupnum
            blind["longitude"] = longitude
            blind["latitude"] = latitude


            db.collection("OnStation").document(stationid).collection("stayUser").document(uid)
                .set(blind)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                    switchActivity(RideBus::class.java)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    private fun switchActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.putExtra("busNo", currentBusNo) // 전역 변수 사용
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }
}