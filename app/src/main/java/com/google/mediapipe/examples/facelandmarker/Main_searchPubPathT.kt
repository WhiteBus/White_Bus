package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathResult
import com.google.mediapipe.examples.facelandmarker.remote.service.PathService
import com.google.mediapipe.examples.facelandmarker.remote.view.PathView

class Main_searchPubPathT : AppCompatActivity(), PathView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 출발지와 도착지의 좌표를 설정
        val startStationX = Main_NearestStation.GlobalValue_start.station_longitude
        val startStationY = Main_NearestStation.GlobalValue_start.station_latitude
        val endStation = Main_vi_Search_des.GlobalValues_end.endPointStation

        if (startStationY != null && startStationX != null && endStation != null) {
            // Retrofit을 사용하여 API 호출
            val retrofit = RetrofitModule.getRetrofit()
            val pathService = PathService(retrofit)

            // 출발지와 도착지의 좌표를 가져와서 API에 전달
            val SX = startStationX
            val SY = startStationY
            val EX = endStation.x
            val EY = endStation.y

            // API 호출
            pathService.searchPath("0", SX, SY, EX, EY, 2, "rfSg7BEmSQsZFsPbMswlSOp5iiDu6smXQXY56n+aR4U", this)
        } else {
            // 출발지 또는 도착지가 설정되지 않은 경우 처리
            println("Please select both start and end stations.")
        }
    }


    override fun onSearchStationSuccess(response: PathResult) {
        val paths = response.result.paths
        val busNumbers = mutableListOf<String>() // 리스트 생성

        for (path in paths) {
            val subPaths = path.subPaths
            for (subPath in subPaths) {
                val lanes = subPath.lane
                lanes?.forEach { lane ->
                    val busNo = lane.busNo
                    if (busNo != null) {
                        busNumbers.add(busNo) // 버스 번호를 리스트에 추가
                    }
                }
            }
        }

        // Main_Bus_Arrival로 이동
        val intent = Intent(this, Main_Bus_Arrival::class.java)
        intent.putStringArrayListExtra("busNumbers", ArrayList(busNumbers)) // 버스 번호 리스트 전달
        startActivity(intent)
        println(ArrayList(busNumbers))
    }

    override fun onSearchStationFailure(errorMessage: String) {
        // 실패 시 처리
        println("Error occurred: $errorMessage")
    }

}



