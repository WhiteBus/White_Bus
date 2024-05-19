package com.google.mediapipe.examples.facelandmarker.remote.view

import com.google.mediapipe.examples.facelandmarker.remote.dto.FindNearestStationGetRes

interface FindNearestStationView {
    fun onFindNearestStationSuccess(response: FindNearestStationGetRes)
    fun onFindNearestStationFailure(errorMessage: String, response: FindNearestStationGetRes?)
}