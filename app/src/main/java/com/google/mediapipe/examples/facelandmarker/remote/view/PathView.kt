package com.google.mediapipe.examples.facelandmarker.remote.view

import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult

interface PathView {
    // 검색 성공 시 호출될 메서드
    fun onSearchStationSuccess(response: PathResult)

    // 검색 실패 시 호출될 메서드
    fun onSearchStationFailure(errorMessage: String)
}