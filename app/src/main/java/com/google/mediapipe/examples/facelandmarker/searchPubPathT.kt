package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathInfoStation
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult
import com.google.mediapipe.examples.facelandmarker.remote.dto.SubPathInfo
import com.google.mediapipe.examples.facelandmarker.remote.service.PathService
import com.google.mediapipe.examples.facelandmarker.remote.view.PathView
import retrofit2.Call

class searchPubPathT : AppCompatActivity(), PathView {

    private lateinit var pathService: PathService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PathService 초기화
        val retrofit = RetrofitModule.getRetrofit()
        pathService = PathService(retrofit)

        // 시작점 = 현위치 가져오기
        val startX = FindCurrentPosition.GlobalValue_current.current_x
        val startY = FindCurrentPosition.GlobalValue_current.current_y

        println("current position : ${startX},${startY}")

        // 끝점 = 검색한 정류장 x,y 가져오기 (방법 1)
        val endStationFromSearch = Main_vi_Search_des.GlobalValues_last.lastPointStation

        // 끝점 = 즐겨찾기로 선택된 지점 (방법 2)
        val endXFromFavorite = intent.getDoubleExtra("endStationX", -1.0)
        val endYFromFavorite = intent.getDoubleExtra("endStationY", -1.0)
        val realName = intent.getStringExtra("realName")

        // 실제 사용되는 끝점
        val (endX, endY) = if (endXFromFavorite != -1.0 && endYFromFavorite != -1.0) {
            endXFromFavorite to endYFromFavorite
        } else if (endStationFromSearch != null) {
            endStationFromSearch.x to endStationFromSearch.y
        } else {
            null to null
        }
        println("Destination is ${endX},${endY}")

        if (startX != null && startY != null && endX != null && endY != null) {
            pathService.searchPath("0", startX, startY, endX, endY, 2, "9pGlz1x7Ic6zBCmZBccmM/QF2qYHiLksHbxjUBdiv3I", this)
        } else {
            println("Please select both start and end stations.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSearchPathSuccess(response: PathResult, startX: Double, startY: Double, endX: Double, endY: Double) {
        val paths = response.result?.paths
        if (paths != null) {
            val pathInfoList = mutableListOf<PathInfoStation>()
            val busNumbersMap = mutableMapOf<Int, MutableList<String>>() // pathIndex별로 bus numbers를 저장할 맵
            val stationIDList = mutableSetOf<Int>() // 시작할 수 있는 stationID를 저장할 세트

            paths.forEachIndexed { pathIndex, path ->
                val subPathInfoList = mutableListOf<SubPathInfo>()
                val subPathMap = mutableMapOf<Int, MutableList<String>>()

                path.subPaths.forEachIndexed { subPathIndex, subPath ->
                    subPath.lane?.forEach { lane ->
                        val busNo = lane.busNo
                        if (busNo != null) {
                            subPathMap.computeIfAbsent(subPathIndex) { mutableListOf() }.add(busNo)

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

                            subPath.startID?.let { stationIDList.add(it) }

                            // pathIndex에 해당하는 busNumbers 리스트에 busNo 추가
                            busNumbersMap.computeIfAbsent(pathIndex) { mutableListOf() }.add(busNo)
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



                // Print path details
                println("Path Index: $pathIndex")
                println("Bus Transit Count: ${path.pathInfo.busTransitCount}")
                println("All Bus Numbers for Path Index $pathIndex: ${busNumbersMap[pathIndex]?.joinToString(", ")}")
                println("Total Time: ${path.pathInfo.totalTime}")
                println("Total Distance: ${path.pathInfo.totalDistance}")

                subPathMap.forEach { (subPathIndex, busNos) ->
                    val subPathInfo = subPathInfoList.find { it.index == subPathIndex }
                    if (subPathInfo != null) {
                        println("  SubPath Index: $subPathIndex")
                        println("  ${subPathInfo.startName} (${subPathInfo.startID}) to ${subPathInfo.endName} - Bus No: ${busNos.joinToString(", ")}")
                        println("    Distance: ${subPathInfo.distance}")
                        println("    Section Time: ${subPathInfo.sectionTime} minutes")
                        println("    Start Coordinates: (${subPathInfo.startX}, ${subPathInfo.startY})")
                        println("    End Coordinates: (${subPathInfo.endX}, ${subPathInfo.endY})")
                    }
                }
            }

            // 끝점 = 검색한 정류장 이름 가져오기 (xml에 보여줄려고)
            val lastStationName = intent.getStringExtra("laststationname")

            // Main_Bus_Arrival로 이동
            val intent = Intent(this, Main_Bus_Arrival::class.java)
            intent.putIntegerArrayListExtra("stationIDList", ArrayList(stationIDList)) // 중복 제거된 stationID 리스트 전달
            intent.putParcelableArrayListExtra("pathInfoList", ArrayList(pathInfoList))
            //intent.putExtra("endStationName", endStationName)
            Log.d("searchPubPathT",ArrayList(stationIDList).toString())
            Log.d("searchPubPathT",ArrayList(pathInfoList).toString())
            Log.d("searchPubPathT",lastStationName.toString())

            // 각 pathIndex별로 busNumbers를 전달
            busNumbersMap.forEach { (pathIndex, busNumbers) ->
                intent.putStringArrayListExtra("busNumbers_$pathIndex", ArrayList(busNumbers))
            }
            //목적지 station이름
            intent.putExtra("selectedStationName", lastStationName) // 선택된 역 이름을 전달
            Log.d("searchPubPathT",lastStationName.toString())

            startActivity(intent)
        } else {
            println("Error: PathResult paths is null")
        }
    }

    override fun onSearchPathFailure(errorMessage: String) {
        println("Error occurred: $errorMessage")
    }

}


