package com.google.mediapipe.examples.facelandmarker.remote.dto

data class TransitInfo(
    val busNo: String,
    val startName: String,
    val endName: String,
    val distance: Double,
    val sectionTime: Int
)