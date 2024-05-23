package com.google.mediapipe.examples.facelandmarker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.remote.adapter.StationAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.FindNearestStationGetRes
import com.google.mediapipe.examples.facelandmarker.remote.dto.Station
import com.google.mediapipe.examples.facelandmarker.remote.service.FindNearestStationService
import com.google.mediapipe.examples.facelandmarker.remote.view.FindNearestStationView

class Main_NearestStation : AppCompatActivity(), FindNearestStationView, StationAdapter.OnItemClickListener {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StationAdapter
    private lateinit var findNearestStationService: FindNearestStationService
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vi_nearest_station)

        auth = FirebaseAuth.getInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationButton: ImageButton = findViewById(R.id.locationButton)
        locationButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            val latitude = 37.49797//it.latitude
                            val longitude = 127.0276//it.longitude
                            Log.e("Location", "Latitude: $latitude, Longitude: $longitude")

                            showRecyclerView(latitude, longitude)
                        } ?: run {
                            Log.e("Location", "Failed to get location")
                            Toast.makeText(
                                this,
                                "위치 정보를 가져올 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Location", "Failed to get location: ${e.message}")
                        Toast.makeText(
                            this,
                            "위치 정보를 가져오는 데 실패했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
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

        val lang = "0"
        val radius = 10000
        val stationClass = 1
        val apiKey = "9pGlz1x7Ic6zBCmZBccmM/QF2qYHiLksHbxjUBdiv3I"

        findNearestStationService.getNearestStation(lang, longitude, latitude, radius, stationClass, apiKey)

        adapter.setOnItemClickListener(this)
    }

    override fun onFindNearestStationSuccess(response: FindNearestStationGetRes) {
        adapter.setStationList(response.result.station)
    }

    override fun onFindNearestStationFailure(errorMessage: String, response: FindNearestStationGetRes?) {
        Toast.makeText(this, "Failed to fetch nearest stations: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(station: Station) {
        GlobalValue_first.stationID = station.stationID
        GlobalValue_first.station_longitude = station.x
        GlobalValue_first.station_latitude = station.y

        println("출발지 정보:")
        println("위도: ${station.y}, 경도: ${station.x}, Station ID: ${GlobalValue_first.stationID}")

        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["longitude"] = station.x
            blind["latitude"] = station.y
            blind["stationID"] = station.stationID
            val userRef = db.collection("BlindUser").document(uid)
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

    object GlobalValue_first {
        var station_longitude: Double? = null
        var station_latitude: Double? = null
        var stationID: Int? = null
    }
}