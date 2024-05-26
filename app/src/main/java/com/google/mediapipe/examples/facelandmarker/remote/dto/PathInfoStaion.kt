package com.google.mediapipe.examples.facelandmarker.remote.dto

import android.os.Parcel
import android.os.Parcelable

data class PathInfoStation(
    val index: Int,
    val busTransitCount: Int,
    val firstStartStation: String,
    val firstStartStationX: Double,
    val firstStartStationY: Double,
    val lastEndStation: String,
    val lastEndStationX: Double,
    val lastEndStationY: Double,
    val totalTime: Int,
    val totalDistance: Int,
    val subPaths: List<SubPathInfo>

) : Parcelable {
    constructor(parcel: Parcel) : this(
        index = parcel.readInt(),
        busTransitCount = parcel.readInt(),
        firstStartStation = parcel.readString() ?: "Unknown",
        firstStartStationX = parcel.readDouble(),
        firstStartStationY = parcel.readDouble(),
        lastEndStation = parcel.readString() ?: "Unknown",
        lastEndStationX = parcel.readDouble(),
        lastEndStationY = parcel.readDouble(),
        totalTime = parcel.readInt(),
        totalDistance = parcel.readInt(),
        subPaths = parcel.createTypedArrayList(SubPathInfo.CREATOR) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
        parcel.writeInt(busTransitCount)
        parcel.writeString(firstStartStation)
        parcel.writeDouble(firstStartStationX)
        parcel.writeDouble(firstStartStationY)
        parcel.writeString(lastEndStation)
        parcel.writeDouble(lastEndStationX)
        parcel.writeDouble(lastEndStationY)
        parcel.writeInt(totalTime)
        parcel.writeInt(totalDistance)
        parcel.writeTypedList(subPaths)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<PathInfoStation> {
        override fun createFromParcel(parcel: Parcel): PathInfoStation {
            return PathInfoStation(parcel)
        }

        override fun newArray(size: Int): Array<PathInfoStation?> {
            return arrayOfNulls(size)
        }
    }
}

