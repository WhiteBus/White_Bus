package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private const val TAG = "app_user_determinant"

class app_user_determinant : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var isBlindTypeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_user_determinant)

        auth = FirebaseAuth.getInstance()

        val busdriverbtn = findViewById<ImageView>(R.id.bus_driver_button)
        val blindbtn = findViewById<ImageView>(R.id.visual_impaired_button)

        busdriverbtn.setOnClickListener {
            if (!isBlindTypeSelected) {
                val a = updateUserBlindType(1)
                transferUserData(a)
                val intent = Intent(this@app_user_determinant, Bus_Number_input::class.java)
                startActivity(intent)
            }
        }

        blindbtn.setOnClickListener {
            if (!isBlindTypeSelected) {
                val a = updateUserBlindType(2)
                transferUserData(a)
                val intent = Intent(this@app_user_determinant, RideBus::class.java)
                startActivity(intent)
            }

        }

    }
    //user 컬렉션의 사용자들을 blind와 dirver로 나누는 함수
    fun transferUserData(blindtype: Int) {
        Log.d(TAG, "entered collection ")

        val uid = auth.currentUser?.uid ?: return
        val blindType = blindtype
        val userDocRef = db.collection("users").document(uid)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        val nickname = it["nickname"] as? String
                        val profileImageUrl = it["profileImageUrl"] as? String

                        if (blindType == 1) {
                            if (nickname != null) {
                                addDriver(nickname)
                            }
                        } else if (blindType == 2) {
                            if (nickname != null) {
                                if (profileImageUrl != null) {
                                    addBlind(uid, nickname, profileImageUrl)
                                }
                            }
                        }else{
                            println(blindType)
                            println(nickname)
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
    //driver 컬렉션에 유저 이동
    fun addDriver( nickname: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val driver: MutableMap<String, Any> = HashMap()
            driver["nickname"] = nickname
            db.collection("DriverUser").document(uid)
                .set(driver)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
    //blind 컬렉션에 유저 이동
    fun addBlind(uid: String, nickname: String, profileImageUrl: String) {
        println('4')
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["nickname"] = nickname
            blind["profileImageUrl"] = profileImageUrl

            db.collection("BlindUser").document(uid)
                .set(blind)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
    private fun updateUserBlindType(isBlind: Int): Int {
        val user = auth.currentUser
        user?.let {
            val userRef = db.collection("users").document(it.uid)
            userRef.update("blindtype", isBlind)
                .addOnSuccessListener {
                    Log.d(TAG, "Blind type updated successfully.")
                    isBlindTypeSelected = true
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to update blind type.${e.message}", e)
                }
        }
        return isBlind
    }
}
