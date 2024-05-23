package com.google.mediapipe.examples.facelandmarker.remote.service

import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult
import com.google.mediapipe.examples.facelandmarker.remote.retrofit.PathInterface
import com.google.mediapipe.examples.facelandmarker.remote.view.PathView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PathService(private val retrofit: Retrofit) {
    fun searchPath(lang: String, SX: Double, SY: Double, EX: Double, EY: Double, searchPathType: Int, apiKey: String, pathView: PathView) {
        val service = retrofit.create(PathInterface::class.java)
        val call = service.getpathBusNum(lang, SX, SY, EX, EY, searchPathType, apiKey)

        call.enqueue(object : Callback<PathResult> {
            override fun onResponse(call: Call<PathResult>, response: Response<PathResult>) {
                if (response.isSuccessful) {
                    val pathResult = response.body()
                    if (pathResult != null) {
                        pathView.onSearchPathSuccess(pathResult, SX, SY, EX, EY)
                    } else {
                        pathView.onSearchPathFailure("Response body is null")
                    }
                } else {
                    pathView.onSearchPathFailure(response.message())
                }
            }

            override fun onFailure(call: Call<PathResult>, t: Throwable) {
                pathView.onSearchPathFailure(t.message ?: "Unknown error")
            }
        })
    }
}