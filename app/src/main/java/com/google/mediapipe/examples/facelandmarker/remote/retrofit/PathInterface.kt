package com.google.mediapipe.examples.facelandmarker.remote.retrofit

import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PathInterface {
    @GET("searchPubTransPath")
    fun getpathBusNum(
        @Query("lang") lang: String, // 언어 코드 (예: "0")
        @Query("SX") SX:Double,	//출발지 X좌표 (경도좌표)
        @Query("SY") SY:Double, //출발지 Y좌표 (위도좌표)
        @Query("EX") EX:Double,//도착지 X좌표 (경도좌표)
        @Query("EY") EY:Double, //도착지 Y좌표 (위도좌표)
        @Query("SearchPathType") SearchPathType:Int, //도시 내 경로수단을 지정 (버스 = 2)
        @Query("apiKey") apiKey: String // API 키
    ): Call<PathResult>
}