package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.remote.adapter.TransitAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.BusArrival
import com.google.mediapipe.examples.facelandmarker.remote.dto.PathInfoStation
import com.google.mediapipe.examples.facelandmarker.remote.dto.RealtimeBusArrivalRes
import com.google.mediapipe.examples.facelandmarker.remote.dto.TransitInfo
import com.google.mediapipe.examples.facelandmarker.remote.retrofit.BusArrivalInterface
import com.google.mediapipe.examples.facelandmarker.remote.service.RealTimeService
import com.google.mediapipe.examples.facelandmarker.remote.view.RealTimeView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Main_Bus_Arrival : AppCompatActivity(), RealTimeView {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private var call: Call<RealtimeBusArrivalRes>? = null
    private lateinit var realtimeAdapter: TransitAdapter
    private lateinit var realTimeService: RealTimeService
    private lateinit var transit_recyclerView: RecyclerView
    private lateinit var TotAdress: TextView
    private lateinit var TotTime: TextView
    private lateinit var TotDistance: TextView

    companion object {
        var globalRouteNm: String? = null
        var globalBusPlateNo: String? = null
        var globalTransitCnt: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_address)
        auth = Firebase.auth
        transit_recyclerView = findViewById(R.id.transit_recyclerView)
        TotAdress = findViewById(R.id.user_address_dst_name)
        TotTime = findViewById(R.id.user_time)
        TotDistance = findViewById(R.id.user_distance)

        transit_recyclerView.layoutManager = LinearLayoutManager(this)

        realtimeAdapter = TransitAdapter(emptyList()) // 어댑터를 빈 리스트로 초기화
        transit_recyclerView.adapter = realtimeAdapter

        val retrofit = RetrofitModule.getRetrofit()
        realTimeService = RealTimeService(retrofit)
        realTimeService.setRealtimeBusArrival(this)

        // Main_vi_Search_des의 GlobalValues_last에서 lastPointStation의 stationName을 가져와 TotAdress에 설정
        val lastPointStation = Main_vi_Search_des.GlobalValues_last.lastPointStation
        TotAdress.text = lastPointStation?.stationName ?: "Station name not available"

        // 각각의 TextView 지정
        val busNumbers = intent.getStringArrayListExtra("busNumbers") ?: arrayListOf()
        val stationID = intent.getIntExtra("startID", -1)
        val pathInfoList = intent.getParcelableArrayListExtra<PathInfoStation>("pathInfoList") ?: arrayListOf()

        println("stationID : $stationID")
        if (stationID != -1) {
            fetchBusArrivalData(stationID, busNumbers) {
                // 필요한 모든 데이터를 처리한 후 필요한 작업을 완료합니다.
                selectAndPrintRoute(globalRouteNm, pathInfoList)
            }
        } else {
            println("Station ID is not available.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
    }

    private fun fetchBusArrivalData(stationID: Int, busNumbers: List<String>, onFetchComplete: () -> Unit) {
        val retrofit = RetrofitModule.getRetrofit()
        val busArrivalService = retrofit.create(BusArrivalInterface::class.java)
        call = busArrivalService.getRealtimeBusArrival(lang = 0, stationID = stationID, apiKey = "9pGlz1x7Ic6zBCmZBccmM/QF2qYHiLksHbxjUBdiv3I")

        call?.enqueue(object : Callback<RealtimeBusArrivalRes> {
            override fun onResponse(call: Call<RealtimeBusArrivalRes>, response: Response<RealtimeBusArrivalRes>) {
                if (response.isSuccessful) {
                    val busArrivalResponse = response.body()
                    if (busArrivalResponse != null && busArrivalResponse.result != null) {
                        val busArrivalList = busArrivalResponse.result.real.filter { busNumbers.contains(it.routeNm) }
                        if (busArrivalList.isNotEmpty()) {
                            findBusWithSmallestArrivalSec(busArrivalList)
                            onFetchComplete()
                        } else {
                            println("No bus arrivals found.")
                        }
                    } else {
                        println("Response is null or result is null.")
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RealtimeBusArrivalRes>, t: Throwable) {
                if (!call.isCanceled) {
                    t.printStackTrace()
                }
            }
        })
    }

    private fun findBusWithSmallestArrivalSec(busArrivalList: List<BusArrival>) {
        val busWithSmallestArrivalSec = busArrivalList
            .filter { it.arrival1 != null }
            .minByOrNull { it.arrival1?.arrivalSec ?: Int.MAX_VALUE }

        busWithSmallestArrivalSec?.let {
            globalBusPlateNo = it.arrival1?.busPlateNo
            globalRouteNm = it.routeNm
            println("The bus with the smallest arrivalSec is: ${it.arrival1?.busPlateNo} (Route: ${it.routeNm})")
        }
        Log.d("hi", "1")

        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            Log.d("hi", "2")

            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            val num = globalBusPlateNo.toString().substring(globalBusPlateNo.toString().length-4,globalBusPlateNo.toString().length)
            blind["pickupNum"] = num
            val userRef = db.collection("BlindUser").document(uid)
            Log.d("hi", "3")

            userRef.update(blind)
                .addOnSuccessListener {
                    Log.d("hi", "Blind type updated successfully.")
                }
                .addOnFailureListener { e ->
                    Log.e("hi", "Failed to update blind type. ${e.message}", e)
                }
        } ?: run {
            Log.e("FirebaseAuth", "User is not authenticated.")
            Toast.makeText(this, "사용자가 인증되지 않았습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
            // 인증이 안된 경우 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
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

            globalTransitCnt = selectedPathInfo.busTransitCount

            // TextView에 Total Time과 Total Distance 설정
            TotTime.text = "${selectedPathInfo.totalTime} min"
            TotDistance.text = "${(selectedPathInfo.totalDistance)/1000} km"

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
            transit_recyclerView.adapter = realtimeAdapter

            for (subPath in filteredSubPaths) {
                println("  SubPath Index: ${subPath.index}")
                println("  ${subPath.startName} (${subPath.startID}) to ${subPath.endName} - Bus No: ${subPath.busNo}")
                println("    Distance: ${subPath.distance}")
                println("    Section Time: ${subPath.sectionTime} minutes")
                println("    Start Coordinates: (${subPath.startX}, ${subPath.startY})")
                println("    End Coordinates: (${subPath.endX}, ${subPath.endY})")
            }
        } else {
            println("No matching route found.")
        }
    }

    override fun onRealTimeSuccess(response: RealtimeBusArrivalRes, stationID: Int) {
        // 성공 시 처리
        val busArrivalList = response.result?.real ?: emptyList()
        val busInfo = busArrivalList.joinToString(separator = "\n") { bus ->
            "Bus No: ${bus.routeNm}, Arrival: ${bus.arrival1?.arrivalSec} sec"
        }
        runOnUiThread {
            TotAdress.text = "Bus Arrivals:\n$busInfo"
            TotTime.text = "Update successful" // 예시 텍스트
            TotDistance.text = "Distance info updated" // 예시 텍스트
        }
    }

    override fun onRealTimeFailure(errorMessage: String) {
        // 실패 시 처리
        runOnUiThread {
            TotAdress.text = "Error: $errorMessage"
            TotTime.text = "Update failed"
            TotDistance.text = "Distance info failed"
        }
    }
}
