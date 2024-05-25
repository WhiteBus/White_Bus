package com.google.mediapipe.examples.facelandmarker.remote.dto

import android.os.Parcel
import android.os.Parcelable

data class SubPathInfo(
    val index: Int,
    val distance: Double,
    val sectionTime: Int,
    val startName: String,
    val startX: Double?,
    val startY: Double?,
    val startID: Int?,
    val endName: String,
    val endX: Double?,
    val endY: Double?,
    val busNo: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        index = parcel.readInt(),
        distance = parcel.readDouble(),
        sectionTime = parcel.readInt(),
        startName = parcel.readString() ?: "Unknown",
        startX = parcel.readDouble(),
        startY = parcel.readDouble(),
        startID = parcel.readInt(),
        endName = parcel.readString() ?: "Unknown",
        endX = parcel.readDouble(),
        endY = parcel.readDouble(),
        busNo = parcel.readString() ?: "Unknown"
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
        parcel.writeDouble(distance)
        parcel.writeInt(sectionTime)
        parcel.writeString(startName)
        if (startX != null) {
            parcel.writeDouble(startX)
        }
        if (startY != null) {
            parcel.writeDouble(startY)
        }
        if (startID != null) {
            parcel.writeInt(startID)
        }
        parcel.writeString(endName)
        if (endX != null) {
            parcel.writeDouble(endX)
        }
        if (endY != null) {
            parcel.writeDouble(endY)
        }
        parcel.writeString(busNo)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<SubPathInfo> {
        override fun createFromParcel(parcel: Parcel): SubPathInfo {
            return SubPathInfo(parcel)
        }

        override fun newArray(size: Int): Array<SubPathInfo?> {
            return arrayOfNulls(size)
        }
    }
}