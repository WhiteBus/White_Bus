package com.google.mediapipe.examples.facelandmarker.remote.retrofit
import com.google.mediapipe.examples.facelandmarker.remote.dto.FindNearestStationGetRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FindNearestStationInterface {
    @GET("pointSearch")
    fun getNearestStation(
        @Query("lang") lang: String, // 언어 코드 (예: "0")
        @Query("x") longitude: Double, // 경도
        @Query("y") latitude: Double, // 위도
        @Query("radius") radius: Int, // 검색 반경
        @Query("stationClass") stationClass: Int, // 역 종류
        @Query("apiKey") apiKey: String // API 키
    ): Call<FindNearestStationGetRes>
}
