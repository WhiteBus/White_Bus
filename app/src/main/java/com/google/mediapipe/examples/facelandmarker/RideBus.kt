package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.Main_Bus_Arrival.Companion.globaltotadress

private const val TAG = "RIdeBus"


class RideBus : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private lateinit var busNoTextView: TextView
//    private lateinit var profileImageUrlTextView: TextView
//    private lateinit var stationIDTextView: TextView
    private lateinit var selectedStationNameTextView: TextView
    private lateinit var ridingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wait_bus)
        auth = FirebaseAuth.getInstance()

        busNoTextView = findViewById(R.id.busNum_btn_wait)
        selectedStationNameTextView = findViewById(R.id.user_address_dst_name)
        ridingButton = findViewById(R.id.riding_btn)

        // Intent로부터 데이터 가져오기
        val busNo = intent.getStringExtra("busNo") ?: "Unknown"
        val profileImageUrl = intent.getStringExtra("profileImageUrl") ?: "Unknown"
        val stationID = intent.getStringExtra("stationID") ?: "Unknown"
        val selectedStationName = globaltotadress

        // TextView에 데이터 설정
        busNoTextView.text = busNo
        selectedStationNameTextView.text = selectedStationName
//        profileImageUrlTextView.text = profileImageUrl

        // Firebase Firestore에서 데이터 가져오기
        db.collection("DriverUser")
            .whereEqualTo("busNumb", busNo)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 문서에서 필드 값 가져오기
                    val fieldData = document.data["busNumb"]
                    println("Field Value: $fieldData")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "get failed with ", e)
            }

        val userid = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("OnStation").document(stationID).collection("stayUser").document(userid)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    val data = documentSnapshot.data
                    data?.let {
                        val profileImageUrl = (it["profileImageUrl"] as? String).toString()
                        println("User data: $profileImageUrl")

                        // TextView에 데이터 설정
//                        profileImageUrlTextView.text = profileImageUrl
                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
        println("User data: $profileImageUrl")

        ridingButton.setOnClickListener {
            if (profileImageUrl.isNotEmpty()) {
                addBlindToBus(busNo, profileImageUrl, stationID)
            }
            val intent = Intent(this@RideBus, user_dropin_bus::class.java)
            startActivity(intent)
        }
    }

    private fun addBlindToBus(busNumber: String, profileImageUrl: String, stationID: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["profileImageUrl"] = profileImageUrl

            // removeBlindToStation 함수를 호출하여 스테이션에서 사용자 제거
            removeBlindToStation(uid, stationID)

            // OnBus 컬렉션에 사용자 추가
            db.collection("OnBus").document(busNumber).collection("blindUser").document(uid)
                .set(blind)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $uid")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    private fun removeBlindToStation(uid: String, stationID: String) {
        // OnStation 컬렉션에서 사용자 제거
        db.collection("OnStation").document(stationID).collection("stayUser").document(uid)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted! :$uid")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }
}