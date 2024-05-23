package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "RIdeBus"

class RideBus : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wait_bus)
        auth = FirebaseAuth.getInstance()

        val uid = auth.uid

        val ridingbtn = findViewById<Button>(R.id.riding_btn)
        var busNumber = 303 //버스 번호판 네자리 가져오기
        var nickname:String = ""
        var profileImageUrl:String = ""
        var stationid = 123.toString() //Main_NearestStation.GlobalValue_first.stationID.toString()


//커렌트 유저블라인드해서 밑에 함수 적용
        db.collection("DriverUser")
            .whereEqualTo("busNumb", busNumber)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 문서에서 필드 값 가져오기
                    val fieldData = document.data["busNumb"]

                    println("Field Value: $fieldData")
                }
            }
            .addOnFailureListener { e->
                Log.d(TAG, "get failed with ", e)
            }
        val userid = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("OnStation").document(stationid).collection("stayUser").document(
            uid.toString()
        )

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    val data = documentSnapshot.data
                    data?.let {
                        nickname = (it["nickname"] as? String).toString()
                        profileImageUrl = (it["profileImageUrl"] as? String).toString()
                        println("Error getting user data: $nickname, $profileImageUrl")

                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
        println("Error getting user data: $nickname, $profileImageUrl")

        ridingbtn.setOnClickListener{
            if (nickname != null && profileImageUrl != null) {
                addBlindToBus(busNumber.toString(), nickname, profileImageUrl, stationid)
            }
            val intent = Intent(this@RideBus, user_dropin_bus::class.java)
            startActivity(intent)
        }
    }

    fun addBlindToBus(busnumber: String, nickname: String, profileImageUrl: String, stationid: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl

            // removeBlindToStation 함수를 호출하여 스테이션에서 사용자 제거
            removeBlindToStation(uid, stationid)

            // OnBus 컬렉션에 사용자 추가
            db.collection("OnBus").document(busnumber).collection("blindUser").document(uid)
                .set(blind)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $uid")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun removeBlindToStation(uid: String, stationid: String) {
        // OnStation 컬렉션에서 사용자 제거
        db.collection("OnStation").document(stationid).collection("stayUser").document(uid)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted! :$uid")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }
}