package com.google.mediapipe.examples.facelandmarker
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient

//class Main_NearestStation : AppCompatActivity(), FindNearestStationView, StationAdapter.OnItemClickListener {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: StationAdapter
//    private lateinit var findNearestStationService: FindNearestStationService
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_vi_nearest_station)
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        val locationButton: ImageButton = findViewById(R.id.locationButton)
//        locationButton.setOnClickListener {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                fusedLocationClient.lastLocation
//                    .addOnSuccessListener { location: Location? ->
//                        location?.let {
//                            val latitude = it.latitude
//                            val longitude = it.longitude
//                            Log.e("Location", "Latitude: $latitude, Longitude: $longitude")
//
//                            showRecyclerView(latitude, longitude)
//                        } ?: run {
//                            Log.e("Location", "Failed to get location")
//                            Toast.makeText(
//                                this,
//                                "위치 정보를 가져올 수 없습니다.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        Log.e("Location", "Failed to get location: ${e.message}")
//                        Toast.makeText(
//                            this,
//                            "위치 정보를 가져오는 데 실패했습니다.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//                )
//            }
//        }
//    }
//
//    private fun showRecyclerView(latitude: Double, longitude: Double) {
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = StationAdapter()
//        recyclerView.adapter = adapter
//
//        Log.e("Location", "Latitude: $latitude, Longitude: $longitude")
//        findNearestStationService = FindNearestStationService()
//        findNearestStationService.setNearestStationGetView(this)
//
//        val lang = "0"
//        val radius = 10000
//        val stationClass = 1
//        val apiKey = "9pGlz1x7Ic6zBCmZBccmM/QF2qYHiLksHbxjUBdiv3I"
//
//        findNearestStationService.getNearestStation(lang, longitude, latitude, radius, stationClass, apiKey)
//
//        adapter.setOnItemClickListener(this)
//    }
//
//    override fun onFindNearestStationSuccess(response: FindNearestStationGetRes) {
//        adapter.setStationList(response.result.station)
//    }
//
//    override fun onFindNearestStationFailure(errorMessage: String, response: FindNearestStationGetRes?) {
//        Toast.makeText(this, "Failed to fetch nearest stations: $errorMessage", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onItemClick(station: Station) {
//        GlobalValue_first.stationID = station.stationID
//        GlobalValue_first.station_longitude = station.x
//        GlobalValue_first.station_latitude = station.y
//
//        println("출발지 정보:")
//        println("위도: ${station.y}, 경도: ${station.x}, Station ID: ${GlobalValue_first.stationID}")
//
//        val intent = Intent(this, Main_vi_Search_des::class.java).apply {
//            putExtra("startStationX", station.x)
//            putExtra("startStationY", station.y)
//            putExtra("startStationID", station.stationID)
//        }
//        startActivity(intent)
//    }
//
//    companion object {
//        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//    }
//
//    object GlobalValue_first {
//        var station_longitude: Double? = null
//        var station_latitude: Double? = null
//        var stationID: Int? = null
//    }
//}