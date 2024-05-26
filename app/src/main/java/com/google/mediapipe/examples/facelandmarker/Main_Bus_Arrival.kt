package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.FindCurrentPosition.GlobalValue_current.current_x
import com.google.mediapipe.examples.facelandmarker.FindCurrentPosition.GlobalValue_current.current_y
import com.google.mediapipe.examples.facelandmarker.remote.adapter.TransitAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.BusArrival
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathInfoStation
import com.google.mediapipe.examples.facelandmarker.remote.dto.RealtimeBusArrivalRes
import com.google.mediapipe.examples.facelandmarker.remote.dto.TransitInfo
import com.google.mediapipe.examples.facelandmarker.remote.retrofit.BusArrivalInterface

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class Main_Bus_Arrival : AppCompatActivity(), TextToSpeech.OnInitListener {
    //firbase에서 쓸 변수들!!
    companion object {
        var globalRouteNm: String? = null
        var globalBusPlateNo: String? = null
        private var busTransitCount: Int = 0
        var globalstartID: Int = 0
        var globalstartX: Double = 0.0
        var globalstartY: Double = 0.0
        var globaltotadress: String? = null
        private const val SPEECH_REQUEST_CODE = 123
    }

    private lateinit var busArrivalService: BusArrivalInterface
    private var call: Call<RealtimeBusArrivalRes>? = null
    private lateinit var realtimeAdapter: TransitAdapter

    private lateinit var totAddress: TextView
    private lateinit var totTime: TextView
    private lateinit var totDistance: TextView
    private lateinit var transitRecyclerView: RecyclerView

    //누르면 얼마나 남았는 지 말하기  "몇 미터 남았습니다"
    private lateinit var voiceoutput: ImageButton
    private lateinit var tts: TextToSpeech

    private val stationIDList by lazy {
        intent.getIntegerArrayListExtra("stationIDList") ?: arrayListOf()
    }
    private val pathInfoList by lazy {
        intent.getParcelableArrayListExtra<PathInfoStation>("pathInfoList")
            ?: arrayListOf<PathInfoStation>()
    }
    private val busNumbersMap by lazy { loadBusNumbersMap() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_address) // 사용하려는 레이아웃 파일 설정

        // View 초기화
        totAddress = findViewById(R.id.user_address_dst_name)
        totTime = findViewById(R.id.user_time)
        totDistance = findViewById(R.id.user_distance)
        transitRecyclerView = findViewById(R.id.transit_recyclerView)

        // RecyclerView 초기화
        transitRecyclerView.layoutManager = LinearLayoutManager(this)


        // 도착지 설정된 것 방법1) 검색한 결과 + 햄버거바에 설정한 도착지
        val lastStationName = intent.getStringExtra("selectedStationName")
        Log.d("Main_Bus_Arrival",lastStationName.toString())
        // 도착지 설정된 것 방법2 (즐겨찾기 버튼)
        val realName = intent.getStringExtra("realName")
        Log.d("Main_Bus_Arrival",realName.toString())

        // 선택된 도착지 이름 결정
        val selectedStationName = when {
            !lastStationName.isNullOrEmpty() -> lastStationName
            !realName.isNullOrEmpty() -> realName
            else -> "Unknown Destination"
        }
        // 도착지 이름을 텍스트에 설정
        totAddress.text = selectedStationName

        // selctedStationName을 전역변수로 저장
        globaltotadress = selectedStationName


        println("stationIDList : $stationIDList")
        if (stationIDList.isNotEmpty()) {
            fetchBusArrivalDataForAllStations(stationIDList) {
                // 필요한 모든 데이터를 처리한 후 필요한 작업을 완료합니다.
                selectAndPrintRoute(globalRouteNm, pathInfoList)
            }
        } else {
            println("Station ID list is not available.")
        }

        // 음성출력
        voiceoutput = findViewById(R.id.locationButton)
        tts = TextToSpeech(this, this)
        voiceoutput.setOnClickListener {
            val distanceToFirstStop = calculateDistanceToFirstStop(pathInfoList)
            val textToSpeak = "정류장까지 남은 거리는 ${distanceToFirstStop} 미터 입니다."
            speakOut(textToSpeak)
        }
    }

    private fun calculateDistanceToFirstStop(pathInfoList: List<PathInfoStation>): Int {
        if (pathInfoList.isEmpty()) return 0

        val selectedPathIndex = pathInfoList.indexOfFirst { pathInfo ->
            pathInfo.subPaths.any { subPath -> subPath.busNo == globalRouteNm }
        }

        if (selectedPathIndex != -1) {
            val firstSubPath = pathInfoList[selectedPathIndex].subPaths.firstOrNull()
            if (firstSubPath != null) {
                return calculateDistance(
                    current_y ?: 0.0,
                    current_x ?: 0.0,
                    firstSubPath.startY ?: 0.0,
                    firstSubPath.startX ?: 0.0
                )
            }
        }
        return 0
    }

    // speaking 초기화
    private fun speakOut(text: String) {
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREA)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    private fun loadBusNumbersMap(): Map<Int, List<String>> {
        val map = mutableMapOf<Int, List<String>>()
        for (pathIndex in pathInfoList.indices) {
            val busNumbers =
                intent.getStringArrayListExtra("busNumbers_$pathIndex") ?: arrayListOf()
            map[pathIndex] = busNumbers
        }
        return map
    }

    private fun fetchBusArrivalDataForAllStations(
        stationIDList: List<Int>,
        onFetchComplete: () -> Unit
    ) {
        val retrofit = RetrofitModule.getRetrofit()
        busArrivalService = retrofit.create(BusArrivalInterface::class.java)

        val busArrivalList = mutableListOf<BusArrival>()
        var completedRequests = 0

        stationIDList.forEach { stationID ->
            call = busArrivalService.getRealtimeBusArrival(
                lang = 0,
                stationID = stationID,
                apiKey = "okelebDYDmSn45nkq8Ojn0rFN3Kv8F+sv0Yyr5oSr1s"
            )
            call?.enqueue(object : Callback<RealtimeBusArrivalRes> {
                override fun onResponse(
                    call: Call<RealtimeBusArrivalRes>,
                    response: Response<RealtimeBusArrivalRes>
                ) {
                    if (response.isSuccessful) {
                        val busArrivalResponse = response.body()
                        if (busArrivalResponse != null && busArrivalResponse.result != null) {
                            val arrivals = busArrivalResponse.result.real?.filter {
                                busNumbersMap.values.flatten().contains(it.routeNm)
                            } ?: emptyList()
                            busArrivalList.addAll(arrivals)
                        }
                    } else {
                        println("Error: ${response.code()}")
                    }
                    // 주변에 탈 수 있는 버스 정류장이 많은 경우 (List)로 받아오기
                    completedRequests++
                    if (completedRequests == stationIDList.size) {
                        findBusWithSmallestArrivalSec(busArrivalList)
                        onFetchComplete()
                    }
                }

                override fun onFailure(call: Call<RealtimeBusArrivalRes>, t: Throwable) {
                    if (!call.isCanceled) {
                        t.printStackTrace()
                    }
                    completedRequests++
                    if (completedRequests == stationIDList.size) {
                        findBusWithSmallestArrivalSec(busArrivalList)
                        onFetchComplete()
                    }
                }
            })
        }
    }

    private fun findBusWithSmallestArrivalSec(busArrivalList: List<BusArrival>) {
        val busWithSmallestArrivalSec = busArrivalList
            .filter { it.arrival1 != null }
            .minByOrNull { it.arrival1?.arrivalSec ?: Int.MAX_VALUE }

        busWithSmallestArrivalSec?.let {
            globalBusPlateNo = it.arrival1?.busPlateNo
            globalRouteNm = it.routeNm
            updateArrivalSecInRecyclerView(it.arrival1?.arrivalSec)
            println("The bus with the smallest arrivalSec is: ${it.arrival1?.busPlateNo} (Route: ${it.routeNm})")
        }
    }

    private fun updateArrivalSecInRecyclerView(arrivalSec: Int?) {
        val transitInfos = pathInfoList.flatMap { pathInfo ->
            pathInfo.subPaths.map { subPath ->
                TransitInfo(
                    startName = subPath.startName,
                    endName = subPath.endName,
                    busNo = subPath.busNo,
                    distance = subPath.distance,
                    sectionTime = subPath.sectionTime,
                )
            }
        }
        realtimeAdapter = TransitAdapter(transitInfos)
        transitRecyclerView.adapter = realtimeAdapter
    }

    private fun selectAndPrintRoute(routeNm: String?, pathInfoList: List<PathInfoStation>) {
        if (routeNm == null) {
            println("No route name provided.")
            return
        }

        val selectedPathIndex = pathInfoList.indexOfFirst { pathInfo ->
            pathInfo.subPaths.any { subPath -> subPath.busNo == routeNm }
        }

        if (selectedPathIndex != -1) {
            val selectedPathInfo = pathInfoList[selectedPathIndex]
            println("Selected Route:")
            println("Path Index: $selectedPathIndex")
            println("Bus Transit Count: ${selectedPathInfo.busTransitCount}")
            println("Total Time: ${selectedPathInfo.totalTime}")
            println("Total Distance ${selectedPathInfo.totalDistance}")

            busTransitCount = selectedPathInfo.busTransitCount

            // TextView에 Total Time과 Total Distance 설정
            totTime.text = "${selectedPathInfo.totalTime} min"
            totDistance.text = "${(selectedPathInfo.totalDistance) / 1000} km"

            // 같은 subPathIndex를 가진 것 중 routeNm과 다른 버스들을 제거한 subPaths 리스트 생성
            val filteredSubPaths = selectedPathInfo.subPaths.groupBy { it.index }
                .flatMap { (index, subPaths) ->
                    val filtered = subPaths.filter { subPath -> subPath.busNo == routeNm }
                    if (filtered.isNotEmpty()) filtered else subPaths
                }

            val transitInfos = filteredSubPaths.map { subPath ->
                TransitInfo(
                    startName = subPath.startName,
                    endName = subPath.endName,
                    busNo = subPath.busNo,
                    distance = subPath.distance,
                    sectionTime = subPath.sectionTime
                )
            }

            realtimeAdapter = TransitAdapter(transitInfos)
            transitRecyclerView.adapter = realtimeAdapter

            var counter = 1
            for (subPath in filteredSubPaths) {
                println("  SubPath Index: ${subPath.index}")
                println("  ${subPath.startName} (${subPath.startID}) to ${subPath.endName} - Bus No: ${subPath.busNo}")
                println("    Distance: ${subPath.distance}")
                println("    Section Time: ${subPath.sectionTime} minutes")
                println("    Start Coordinates: (${subPath.startX}, ${subPath.startY}) - Number: $counter")
                println("    End Coordinates: (${subPath.endX}, ${subPath.endY})")

                counter++
            }

            // 추가적으로, globalTransitCnt 값을 사용하여 각 startX에 번호를 매깁니다.
            if (busTransitCount > 0) {
                for (i in 0 until Main_Bus_Arrival.busTransitCount + 1) {
                    if (i < filteredSubPaths.size) {
                        val subPath = filteredSubPaths[i]

                        // firebase에서 쓸 변수들!!

                        globalstartID = subPath.startID!!
                        globalstartX = subPath.startX!!
                        globalstartY = subPath.startY!!
                        println("Global Transit Count Index: $i")
                        println("  Start Coordinates: (${globalstartX}, ${globalstartY}) - Number: ${i + 1}")
                        println("  StartID ffffffirt ${globalstartID}")
                    }
                }
            }
        } else {
            println("No matching route found.")
        }
    }

    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Int {
        val R = 6371.0 // 지구의 반지름 (킬로미터)

        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = sin(deltaPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(deltaLambda / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (R * c * 1000).roundToInt() // 미터 단위로 반환
    }
}