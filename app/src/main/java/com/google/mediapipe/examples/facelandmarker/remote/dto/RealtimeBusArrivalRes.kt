package com.google.mediapipe.examples.facelandmarker.remote.dto
import com.google.gson.annotations.SerializedName

data class RealtimeBusArrivalRes(
    @SerializedName("result")
    val result: Result
)

data class Result(
    @SerializedName("real")
    val real: List<BusArrival>
)

data class BusArrival(
    @SerializedName("routeNm")
    val routeNm: String,
    @SerializedName("arrival1")
    val arrival1: Arrival
)

data class Arrival(
    @SerializedName("busPlateNo")
    val busPlateNo: String,
    @SerializedName("arrivalSec")
    val arrivalSec: Int
)