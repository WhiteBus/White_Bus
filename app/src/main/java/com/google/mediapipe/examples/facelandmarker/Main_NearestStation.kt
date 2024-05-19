package com.google.mediapipe.examples.facelandmarker
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.Adapter.StationAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.FindNearestStationGetRes
import com.google.mediapipe.examples.facelandmarker.remote.dto.Station
import com.google.mediapipe.examples.facelandmarker.remote.service.FindNearestStationService
import com.google.mediapipe.examples.facelandmarker.remote.view.FindNearestStationView

class Main_NearestStation : AppCompatActivity(), FindNearestStationView, StationAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StationAdapter
    private lateinit var findNearestStationService: FindNearestStationService
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vi_nearest_station)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationButton: Button = findViewById(R.id.locationButton)
        locationButton.setOnClickListener {
            // 위치 권한 확인
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // 위치 정보 요청
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            // 위치 정보를 가져왔을 때
                            val latitude = it.latitude
                            val longitude = it.longitude
                            Log.e("Location", "Latitude: $latitude, Longitude: $longitude")

                            showRecyclerView(latitude, longitude)
                        } ?: run {
                            // 위치 정보를 가져오지 못했을 때
                            Log.e("Location", "Failed to get location")
                            Toast.makeText(
                                this,
                                "위치 정보를 가져올 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // 위치 정보를 가져오지 못했을 때
                        Log.e("Location", "Failed to get location: ${e.message}")
                        Toast.makeText(
                            this,
                            "위치 정보를 가져오는 데 실패했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                // 위치 권한이 없는 경우, 사용자에게 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun showRecyclerView(latitude: Double, longitude: Double) {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StationAdapter()
        recyclerView.adapter = adapter

        Log.e("Location", "Latitude: $latitude, Longitude: $longitude")
        findNearestStationService = FindNearestStationService()
        findNearestStationService.setNearestStationGetView(this)

        val lang = "0" // 언어 코드
        val radius = 700 // 검색 반경 (미터)
        val stationClass = 1 // 역 종류 (1: 지하철역)
        val apiKey = "rfSg7BEmSQsZFsPbMswlSOp5iiDu6smXQXY56n+aR4U"

        // FindNearestStationService를 사용하여 역 정보 가져오기
        findNearestStationService.getNearestStation(lang, longitude, latitude, radius, stationClass, apiKey)

        // StationAdapter의 클릭 리스너 설정
        adapter.setOnItemClickListener(this)
    }

    override fun onFindNearestStationSuccess(response: FindNearestStationGetRes) {
        // 서버에서 받은 역 정보를 RecyclerView에 표시
        adapter.setStationList(response.result.station)
    }

    override fun onFindNearestStationFailure(errorMessage: String, response: FindNearestStationGetRes?) {
        // 실패 시 처리
        Toast.makeText(this, "Failed to fetch nearest stations: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(station: Station) {
        // 클릭한 아이템의 stationID를 전역 변수에 저장
        GlobalValue_start.stationID = station.stationID
        // 전역 변수에 경도와 위도 저장
        GlobalValue_start.station_longitude = station.x
        GlobalValue_start.station_latitude = station.y

        // 출발지 정보 출력
        println("출발지 정보:")
        println("위도: ${station.y}, 경도: ${station.x}, Station ID: ${GlobalValue_start.stationID}")

        // 다음 액티비티로 넘어가기
        val intent = Intent(this, Main_vi_Search_des::class.java).apply {
            putExtra("startStationX", station.x)
            putExtra("startStationY", station.y)
            putExtra("startStationID", station.stationID)
        }
        startActivity(intent)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    // 전역 변수 선언
    object GlobalValue_start {
        var station_longitude: Double? = null // 경도
        var station_latitude: Double? = null // 위도
        var stationID: Int? = null // 역 ID
    }
}
