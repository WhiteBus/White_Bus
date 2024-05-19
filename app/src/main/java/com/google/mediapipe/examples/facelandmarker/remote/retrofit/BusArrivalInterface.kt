package com.google.mediapipe.examples.facelandmarker.remote.retrofit

import com.google.mediapipe.examples.facelandmarker.remote.dto.RealtimeBusArrivalRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BusArrivalInterface {
    @GET("realtimeStation")
    fun getRealtimeBusArrival(
        @Query("lang") lang: Int, // 언어 코드 (예: "0")
        @Query("stationID") stationID: Int,
        @Query("apiKey") apiKey: String
    ): Call<RealtimeBusArrivalRes>
}

