package com.google.mediapipe.examples.facelandmarker.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchStationResponse(
    @SerializedName("result") val result: SearchResult
)

data class SearchResult(
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("station") val station: List<SearchStationInfo>
)

data class SearchStationInfo(
    @SerializedName("stationClass") val stationClass: Int,
    @SerializedName("stationName") val stationName: String,
    @SerializedName("stationID") val stationID: Int,
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double,
)
