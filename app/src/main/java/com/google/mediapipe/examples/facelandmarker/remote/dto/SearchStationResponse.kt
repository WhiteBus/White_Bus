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
    @SerializedName("arsID") val arsID: String,
    @SerializedName("busOnlyCentralLane") val busOnlyCentralLane: Int,
    @SerializedName("localStationID") val localStationID: String,
    @SerializedName("ebid") val ebid: String,
    @SerializedName("stationDirectionName") val stationDirectionName: String,
    @SerializedName("businfo") val busInfo: List<BusInfo>
)

data class BusInfo(
    @SerializedName("busClass") val busClass: String,
    @SerializedName("busLocalBlID") val busLocalBlID: String,
    @SerializedName("busNo") val busNo: String
)