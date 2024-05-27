package com.google.mediapipe.examples.facelandmarker
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "RideBus"

class RideBus : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var dropIN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wait_bus)
        auth = FirebaseAuth.getInstance()
        val uid = auth.uid

        val ridingbtn = findViewById<Button>(R.id.riding_btn)
        var busNumber: String = ""
        var nickname: String = ""
        var profileImageUrl: String = ""
        var stationid: String = ""  // 이 부분이 어디에서 설정되는지 확인 필요
        var busdrivernumonbus: String = ""

        // 커렌트 유저 블라인드에서 밑에 함수 적용
        db.collection("BlindUser").document(uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        busNumber = (it["pickupNum"] as? String).toString()
                        nickname = (it["nickname"] as? String).toString()
                        profileImageUrl = (it["profileImageUrl"] as? String).toString()
                        stationid = (it["stationID"] as? String).toString()

                        // 여기서 DriverUser를 가져오는 호출을 합니다.
                        db.collection("DriverUser")
                            .whereEqualTo("busNumb", busNumber)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    // 문서에서 필드 값 가져오기
                                    busdrivernumonbus = document.data["busNumb"].toString()
                                }

                                // 모든 데이터가 설정된 후에 버튼 클릭 리스너를 설정합니다.
                                ridingbtn.setOnClickListener {
                                    if (nickname.isNotEmpty() && profileImageUrl.isNotEmpty() && stationid.isNotEmpty()) {
                                        addBlindToBus(busdrivernumonbus, nickname, profileImageUrl, stationid)
                                        val intent = Intent(this@RideBus, user_dropin_bus::class.java)
                                        intent.putExtra("stationid", stationid)  // stationid 값을 인텐트로 전달
                                        startActivity(intent)
                                    } else {
                                        Log.e(TAG, "Required data is missing: nickname=$nickname, profileImageUrl=$profileImageUrl, stationid=$stationid")
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.d(TAG, "get failed with ", e)
                            }
                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
    }

    private fun addBlindToBus(busnumber: String, nickname: String, profileImageUrl: String, stationid: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl
            blind["dropin?"] = dropIN

            // removeBlindToStation 함수를 호출하여 스테이션에서 사용자 제거
            removeBlindFromStation(uid, stationid)

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

    private fun removeBlindFromStation(uid: String, stationid: String) {
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