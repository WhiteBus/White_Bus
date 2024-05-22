package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Wait_bus"

class OnStationBus : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_wait_bus)
        auth = FirebaseAuth.getInstance()

        val uid = auth.uid

        val ridingbtn = findViewById<Button>(R.id.riding_btn)
        var nickname:String = ""
        var profileImageUrl:String = ""
        val stationid = "123"//정류장 id가져오기


        val userid = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("users").document(userid)

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
                addBlindToStation(stationid, nickname, profileImageUrl)
            }
        }
    }

    fun addBlindToStation(stationid: String?, nickname: String, profileImageUrl: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl

            db.collection("OnStation").document(stationid.toString())
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