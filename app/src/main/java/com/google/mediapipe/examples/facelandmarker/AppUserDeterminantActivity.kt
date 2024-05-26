package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityAppUserDeterminantBinding
import com.google.mediapipe.examples.facelandmarker.repository.activity_home

private const val TAG = "app_user_determinant"

class AppUserDeterminantActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var isBlindTypeSelected = false
    private lateinit var binding: ActivityAppUserDeterminantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppUserDeterminantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.visualImpairedButton.setOnClickListener {
            // Move to activity_home
            if (!isBlindTypeSelected) {
                updateUserBlindType(2) { success ->
                    if (success) {
                        transferUserData(2)
                        switchActivity(activity_home::class.java)
                    }
                }
            }
        }

        binding.busDriverButton.setOnClickListener {
            // Move to BusNumberInputActivity
            if (!isBlindTypeSelected) {
                updateUserBlindType(1) { success ->
                    if (success) {
                        transferUserData(1)
                        switchActivity(BusNumberInputActivity::class.java)
                    }
                }
            }
        }
    }

    // Switch the Activity function
    private fun switchActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }

    private fun updateUserBlindType(isBlind: Int, callback: (Boolean) -> Unit) {
        val user = auth.currentUser
        user?.let {
            val userRef = db.collection("users").document(it.uid)
            userRef.update("blindtype", isBlind)
                .addOnSuccessListener {
                    Log.d(TAG, "Blind type updated successfully.")
                    isBlindTypeSelected = true
                    callback(true)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to update blind type.${e.message}", e)
                    callback(false)
                }
        }
    }

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
                        } else {
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
    fun addDriver(nickname: String) {
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
            blind["longitude"] = ""
            blind["latitude"] = ""
            blind["stationId"] = ""

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
}
