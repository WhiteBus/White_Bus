package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathInfoStation
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult
import com.google.mediapipe.examples.facelandmarker.remote.dto.SubPathInfo
import com.google.mediapipe.examples.facelandmarker.remote.service.PathService
import com.google.mediapipe.examples.facelandmarker.remote.view.PathView
import retrofit2.Call

class searchPubPathT : AppCompatActivity(), PathView {

    private lateinit var pathService: PathService
    private var call: Call<PathResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PathService 초기화
        val retrofit = RetrofitModule.getRetrofit()
        pathService = PathService(retrofit)

        // 시작 좌표와 종료 좌표 설정
        val startCoordinates = listOf(
            Pair(Main_NearestStation.GlobalValue_first.station_longitude, Main_NearestStation.GlobalValue_first.station_latitude)
        )
        val endStation = Main_vi_Search_des.GlobalValues_last.lastPointStation

        if (endStation != null) {
            for (startCoordinate in startCoordinates) {
                val (startX, startY) = startCoordinate
                if (startX != null && startY != null) {
                    val EX = endStation.x
                    val EY = endStation.y
                    pathService.searchPath("0", startX, startY, EX, EY, 2, "9pGlz1x7Ic6zBCmZBccmM/QF2qYHiLksHbxjUBdiv3I", this)
                }
            }
        } else {
            println("Please select both start and end stations.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    override fun onSearchPathSuccess(response: PathResult, startX: Double, startY: Double, endX: Double, endY: Double) {
        val paths = response.result?.paths
        if (paths != null) {
            val pathInfoList = mutableListOf<PathInfoStation>()
            val busNumbers = mutableListOf<String>()
            var startID: Int? = null

            paths.forEachIndexed { pathIndex, path ->
                val subPathInfoList = mutableListOf<SubPathInfo>()
                path.subPaths.forEachIndexed { subPathIndex, subPath ->
                    subPath.lane?.forEach { lane ->
                        val busNo = lane.busNo//.toString()
                        if (busNo != null) {
                            subPathInfoList.add(
                                SubPathInfo(
                                    index = subPathIndex,
                                    distance = subPath.distance,
                                    sectionTime = subPath.sectionTime,
                                    startName = subPath.startName ?: "Unknown",
                                    startX = subPath.startX,
                                    startY = subPath.startY,
                                    startID = subPath.startID,
                                    endName = subPath.endName ?: "Unknown",
                                    endX = subPath.endX,
                                    endY = subPath.endY,
                                    busNo = busNo
                                )
                            )

                            if (Main_NearestStation.GlobalValue_first.stationID == subPath.startID) {
                                busNumbers.add(busNo)
                                startID = subPath.startID
                            }
                        }
                    }
                }
                pathInfoList.add(
                    PathInfoStation(
                        index = pathIndex,
                        busTransitCount = path.pathInfo.busTransitCount,
                        firstStartStation = path.pathInfo.firstStartStation,
                        firstStartStationX = startX,
                        firstStartStationY = startY,
                        lastEndStation = path.pathInfo.lastEndStation,
                        lastEndStationX = endX,
                        lastEndStationY = endY,
                        totalTime = path.pathInfo.totalTime,
                        totalDistance = path.pathInfo.totalDistance,
                        subPaths = subPathInfoList
                    )
                )
            }

            // Main_Bus_Arrival로 이동
            val intent = Intent(this, Main_Bus_Arrival::class.java)
            intent.putStringArrayListExtra("busNumbers", ArrayList(busNumbers)) // 버스 번호 리스트 전달
            intent.putExtra("startID", startID) // 환승시작점 id 전달
            intent.putParcelableArrayListExtra("pathInfoList", ArrayList(pathInfoList))
            startActivity(intent)
            println(ArrayList(busNumbers))
        } else {
            println("Error: PathResult paths is null: ${paths}")
        }
    }

    override fun onSearchPathFailure(errorMessage: String) {
        println("Error occurred: $errorMessage")
    }
}
