package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Wait_bus"

class OnStationUser : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_popup)
        auth = FirebaseAuth.getInstance()

        val uid = auth.uid

        val staybtn = findViewById<Button>(R.id.btn)
        var nickname:String = ""
        var profileImageUrl:String = ""
        val stationid = "123"//정류장 id가져오기
        var pickupnum:String = ""//탈 버스 넘버



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

                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
        staybtn.setOnClickListener{
            if (nickname != null && profileImageUrl!= null && pickupnum != null) {
                addBlindToStation(stationid, nickname, profileImageUrl, pickupnum)
            }
            val intent = Intent(this@OnStationUser, RideBus::class.java)
            startActivity(intent)
        }
    }

    fun addBlindToStation(stationid: String?, nickname: String, profileImageUrl: String, pickupNum:String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl
            blind["pickupNum"] = pickupNum


            db.collection("OnStation").document(stationid.toString()).collection("stayUser").document(uid)
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