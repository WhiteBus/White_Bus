package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Wait_bus"

class RideBus : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wait_bus)
        auth = FirebaseAuth.getInstance()

        val uid = auth.uid

        val busnum = findViewById<EditText>(R.id.busNum_btn)
        val ridingbtn = findViewById<Button>(R.id.riding_btn)
        var busNumber = 303 //버스 번호판 네자리 가져오기
        var nickname:String = ""
        var profileImageUrl:String = ""

//커렌트 유저블라인드해서 밑에 함수 적용
        db.collection("dirverid")
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
        val userDocRef = db.collection("OnStation").document(userid)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        nickname = (it["nickname"] as? String).toString()
                        profileImageUrl = (it["profileImageUrl"] as? String).toString()
                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
        ridingbtn.setOnClickListener{
            if (nickname != null && profileImageUrl!= null) {
                addBlindToBus(busNumber, nickname, profileImageUrl)
            }
        }
    }

    fun addBlindToBus(busnumber: Int?, nickname: String, profileImageUrl: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl

            db.collection("OnBus").document(busnumber.toString()).collection("blindUser").document(uid)
                .set(blind)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
}