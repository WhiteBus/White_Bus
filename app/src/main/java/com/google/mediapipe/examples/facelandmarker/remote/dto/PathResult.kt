package com.google.mediapipe.examples.facelandmarker.remote.dto

import com.google.gson.annotations.SerializedName

data class PathResult(
    @SerializedName("result") val result: SearchInfo
)

data class SearchInfo(
    @SerializedName("searchType") val searchType: Int,
    @SerializedName("outTrafficCheck") val outTrafficCheck: Int,
    @SerializedName("busCount") val busCount: Int,
    @SerializedName("subwayBusCount") val subwayBusCount: Int,
    @SerializedName("pointDistance") val pointDistance: Int,
    @SerializedName("startRadius") val startRadius: Int,
    @SerializedName("endRadius") val endRadius: Int,
    @SerializedName("path") val paths: List<Path>
)

data class Path(
    @SerializedName("pathType") val pathType: Int,
    @SerializedName("info") val pathInfo: PathInfo,
    @SerializedName("subPath") val subPaths: List<SubPath>
)

data class PathInfo(
    @SerializedName("trafficDistance") val trafficDistance: Int,
    @SerializedName("totalWalk") val totalWalk: Int,
    @SerializedName("totalTime") val totalTime: Int,
    @SerializedName("payment") val payment: Int,
    @SerializedName("busTransitCount") val busTransitCount: Int,
    @SerializedName("firstStartStation") val firstStartStation: String,
    @SerializedName("lastEndStation") val lastEndStation: String,
    @SerializedName("totalStationCount") val totalStationCount: Int,
    @SerializedName("busStationCount") val busStationCount: Int,
    @SerializedName("totalDistance") val totalDistance: Int,
    @SerializedName("checkIntervalTime") val checkIntervalTime: Int,
    @SerializedName("checkIntervalTimeOverYn") val checkIntervalTimeOverYn: String,
    @SerializedName("totalIntervalTime") val totalIntervalTime: Int
)

data class SubPath(
    @SerializedName("trafficType") val trafficType: Int,
    @SerializedName("distance") val distance: Int,
    @SerializedName("sectionTime") val sectionTime: Int,
    @SerializedName("stationCount") val stationCount: Int?,
    @SerializedName("lane") val lane: List<Lane>?,
    @SerializedName("intervalTime") val intervalTime: Int?,
    @SerializedName("startName") val startName: String?,
    @SerializedName("startX") val startX: Double?,
    @SerializedName("startY") val startY: Double?,
    @SerializedName("endName") val endName: String?,
    @SerializedName("endX") val endX: Double?,
    @SerializedName("endY") val endY: Double?,
    @SerializedName("startID") val startID: Int?,
    @SerializedName("startStationCityCode") val startStationCityCode: Int?,
    @SerializedName("startStationProviderCode") val startStationProviderCode: Int?,
    @SerializedName("startLocalStationID") val startLocalStationID: String?,
    @SerializedName("startArsID") val startArsID: String?,
    @SerializedName("endID") val endID: Int?,
    @SerializedName("endStationCityCode") val endStationCityCode: Int?,
    @SerializedName("endStationProviderCode") val endStationProviderCode: Int?,
    @SerializedName("endLocalStationID") val endLocalStationID: String?,
    @SerializedName("endArsID") val endArsID: String?,
    @SerializedName("passStopList") val passStopList: PassStopList?
)

data class Lane(
    @SerializedName("busNo") val busNo: String,
    @SerializedName("type") val type: Int,
    @SerializedName("busID") val busID: Int,
    @SerializedName("busLocalBlID") val busLocalBlID: String,
    @SerializedName("busCityCode") val busCityCode: Int,
    @SerializedName("busProviderCode") val busProviderCode: Int
)

data class PassStopList(
    @SerializedName("stations") val stations: List<Station>
)

data class StationInfo(
    @SerializedName("index") val index: Int,
    @SerializedName("stationID") val stationID: Int,
    @SerializedName("stationName") val stationName: String,
    @SerializedName("stationCityCode") val stationCityCode: Int,
    @SerializedName("stationProviderCode") val stationProviderCode: Int,
    @SerializedName("localStationID") val localStationID: String,
    @SerializedName("arsID") val arsID: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String,
    @SerializedName("isNonStop") val isNonStop: String
)
