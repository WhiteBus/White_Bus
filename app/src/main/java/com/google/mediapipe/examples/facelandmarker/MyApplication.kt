package com.google.mediapipe.examples.facelandmarker

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("v4ud3zha1i")
    }
}
