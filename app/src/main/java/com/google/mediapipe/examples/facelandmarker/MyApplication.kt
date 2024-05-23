package com.google.mediapipe.examples.facelandmarker

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("v4ud3zha1i")
        KakaoSdk.init(this, "46d9f78dd50186c6c666f893e65a737b")

    }
}
