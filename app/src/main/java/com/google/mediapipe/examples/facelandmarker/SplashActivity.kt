package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.common.util.Utility


class SplashActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Keyhash", "keyhash : ${Utility.getKeyHash(this)}")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        Log.d("1", "keyhash : ${Utility.getKeyHash(this)}")
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        var keyHash = Utility.getKeyHash(this)
//        Log.i("GlobalApplication", "$keyHash")
//        println(keyHash)
//
    }
}