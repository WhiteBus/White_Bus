package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

private const val TAG = "Wait_bus"

class OnStationUser : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_user_popup)
        auth = FirebaseAuth.getInstance()

        // TextToSpeech 초기화
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.KOREAN
            }
        }

        val uid = auth.uid

        val staybtn = findViewById<Button>(R.id.btn)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        var nickname:String = ""
        var profileImageUrl:String = ""
        var stationid:String = "" //Main_NearestStation.GlobalValue_first.stationID //정류장 id가져오기
        var pickupnum: String =""
        var longitude: String= ""
        var latitude: String = ""


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
            speak("확인 버튼")
            true
            addBlindToStation(stationid, nickname, profileImageUrl, pickupnum, longitude, latitude)
        }
        staybtn.setOnLongClickListener{
            speak("확인 버튼")
            true
        }

        btnCancel.setOnClickListener{
            speak("취소 버튼")
            true
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
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
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }
}