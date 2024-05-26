package com.google.mediapipe.examples.facelandmarker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.mediapipe.examples.facelandmarker.repository.activity_home

class FindCurrentPosition : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_vi_nearest_station)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        Log.e("Location", "Latitude: $latitude, Longitude: $longitude")

                        // Store the location in GlobalValue_first
                        GlobalValue_current.current_x = longitude
                        GlobalValue_current.current_y = latitude
                        val intent = Intent (this, activity_home::class.java)
                        startActivity(intent)
                        finish()

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


    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    object GlobalValue_current {
        var current_x: Double? = null
        var current_y: Double? = null
    }

}
