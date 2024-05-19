package com.google.mediapipe.examples.facelandmarker.remote.dto

import com.google.gson.annotations.SerializedName

data class FindNearestStationGetRes(
    @SerializedName("result") val result: CountStation
)

data class CountStation(
    @SerializedName("count") val count: Int,
    @SerializedName("station") val station: List<Station>
)

data class Station(
    @SerializedName("arsID") val arsID: String,
    @SerializedName("busOnlyCentralLane") val busOnlyCentralLane: Int,
    @SerializedName("ebid") val ebid: String,
    @SerializedName("nonstopStation") val nonstopStation: Int,
    @SerializedName("stationClass") val stationClass: Int,
    @SerializedName("stationID") val stationID: Int,
    @SerializedName("stationName") val stationName: String,
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double
)
