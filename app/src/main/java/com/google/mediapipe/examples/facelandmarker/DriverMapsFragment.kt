package com.google.mediapipe.examples.facelandmarker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentDriverMapsBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import java.util.Locale

class DriverMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDriverMapsBinding
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverMapsBinding.inflate(layoutInflater, container, false)
        mapView = binding.naverMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 로케이션 버튼 생성
        val locationSource = FusedLocationSource(this@DriverMapsFragment, LOCATION_PERMISSION_REQUEST_CODE)
        // 현재 위치
        naverMap.locationSource = locationSource

        // Check if location permission is granted
        if (hasLocationPermission()) {
            // Request location updates
            requestLocationUpdates()
            // 항상 Follow 모드로 설정
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            // Request location permission
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        // Set listener to the map fragment for refreshing current location when clicked
        mapView.setOnClickListener {
            // Check if location permission is granted
            if (hasLocationPermission()) {
                // Request location updates
                requestLocationUpdates()
            } else {
                // Request location permission
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationUpdates() {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    showToast("Latitude: $latitude, Longitude: $longitude")
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    // naverMap이 초기화된 후에 moveCameraToLocation 호출
                    if (::naverMap.isInitialized) {
                        moveCameraToLocation(latitude, longitude)
                    }
                }
            }
            .addOnFailureListener { exception ->
                showToast("Failed to get location: ${exception.message}")
            }
    }

    private fun moveCameraToLocation(latitude: Double, longitude: Double) {
        val cameraPosition = CameraPosition(
            LatLng(latitude, longitude),
            DEFAULT_ZOOM,
            DEFAULT_TILT,
            DEFAULT_BEARING
        )
        naverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If location permission is granted, request location updates
                    requestLocationUpdates()
                } else {
                    // If location permission is denied, show a message to the user
                    showToast("Location permission denied")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val DEFAULT_ZOOM = 15.0
        private const val DEFAULT_TILT = 0.0
        private const val DEFAULT_BEARING = 0.0
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
