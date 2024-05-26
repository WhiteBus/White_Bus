package com.google.mediapipe.examples.facelandmarker.remote.dto

import android.os.Parcel
import com.google.gson.annotations.SerializedName

data class PathResult(
    @SerializedName("result") val result: SearchInfo
)

data class SearchInfo(
    @SerializedName("searchType") val searchType: Int,
    @SerializedName("path") val paths: List<Path>
)

data class Path(
    @SerializedName("pathType") val pathType: Int,
    @SerializedName("info") val pathInfo: PathInfoStation,
    @SerializedName("subPath") val subPaths: List<SubPath>
)

data class PathInfo(
    @SerializedName("trafficDistance") val trafficDistance: Parcel,
    @SerializedName("totalTime") val totalTime: Int,
    @SerializedName("busTransitCount") val busTransitCount: Int,
    @SerializedName("firstStartStation") val firstStartStation: String,
    @SerializedName("lastEndStation") val lastEndStation: String,
    @SerializedName("totalDistance") val totalDistance: Int,
)

data class SubPath(
    @SerializedName("trafficType") val trafficType: Int,
    @SerializedName("distance") val distance: Double,
    @SerializedName("sectionTime") val sectionTime: Int,
    @SerializedName("lane") val lane: List<Lane>?,
    @SerializedName("startName") val startName: String?,
    @SerializedName("startX") val startX: Double?,
    @SerializedName("startY") val startY: Double?,
    @SerializedName("endName") val endName: String?,
    @SerializedName("endX") val endX: Double?,
    @SerializedName("endY") val endY: Double?,
    @SerializedName("startID") val startID: Int?,
    @SerializedName("endStationProviderCode") val endStationProviderCode: Int?,
    @SerializedName("endLocalStationID") val endLocalStationID: String?,
)

data class Lane(
    @SerializedName("busNo") val busNo: String,
    @SerializedName("busID") val busID: Int,
)