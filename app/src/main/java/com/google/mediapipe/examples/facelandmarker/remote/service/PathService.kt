package com.google.mediapipe.examples.facelandmarker.remote.service

import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult
import com.google.mediapipe.examples.facelandmarker.remote.retrofit.PathInterface
import com.google.mediapipe.examples.facelandmarker.remote.view.PathView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PathService(private val retrofit: Retrofit) {

    private val pathInterface: PathInterface = retrofit.create(PathInterface::class.java)

    fun searchPath(
        lang: String,
        SX: Double,
        SY: Double,
        EX: Double,
        EY: Double,
        SearchPathType: Int,
        apiKey: String,
        callback: PathView
    ) {
        pathInterface.getpathBusNum(lang, SX, SY, EX, EY, SearchPathType, apiKey)
            .enqueue(object : Callback<PathResult> {
                override fun onResponse(call: Call<PathResult>, response: Response<PathResult>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback.onSearchStationSuccess(it)
                        } ?: run {
                            callback.onSearchStationFailure("Response body is null.")
                        }
                    } else {
                        callback.onSearchStationFailure("Failed to get response.")
                    }
                }

                override fun onFailure(call: Call<PathResult>, t: Throwable) {
                    callback.onSearchStationFailure(t.message ?: "Unknown error occurred.")
                }
            })
    }
}