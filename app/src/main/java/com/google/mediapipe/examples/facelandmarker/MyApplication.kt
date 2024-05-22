package com.google.mediapipe.examples.facelandmarker

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
class MyApplication() : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "46d9f78dd50186c6c666f893e65a737b")
    }
}