package com.google.mediapipe.examples.facelandmarker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityDriverMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import java.util.*

class DriverMainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: ActivityDriverMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val marker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        setBottomNavigationView()
    }

    private fun initMapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.main_container) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.main_container, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.setOnMapClickListener { _, coord ->
            addMarkerToMap(coord.latitude, coord.longitude)
        }

        // 초기 마커 추가 (서울의 좌표)
        addMarkerToMap(37.5665, 126.9780)
    }

    private fun addMarkerToMap(lat: Double, lng: Double) {
        val marker = Marker()
        marker.position = LatLng(lat, lng)
        marker.icon = OverlayImage.fromResource(R.drawable.people) // 커스텀 아이콘 설정
        marker.map = naverMap

        marker.setOnClickListener {
            // 마커 클릭 시 다이얼로그 표시
            showMarkerDialog()
            true
        }

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng))
        naverMap.moveCamera(cameraUpdate)
        naverMap.moveCamera(CameraUpdate.zoomTo(18.0))

        getAddress(lat, lng)
    }

    private fun showMarkerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialog_confirmlist)
        val dialog = builder.create()
        dialog.show()
        // 여기에 버튼 클릭 리스너 추가해야함
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(applicationContext, Locale.KOREAN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude, longitude, 1
            ) { address ->
                if (address.size != 0) {
                    toast(address[0].getAddressLine(0))
                }
            }
        } else {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                toast(addresses[0].getAddressLine(0))
            }
        }
    }

    private fun toast(text: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_maps -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, DriverMapsFragment()).commit()
                    true
                }

                R.id.fragment_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, DriverProfileFragment()).commit()
                    true
                }

                R.id.fragment_exit -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, DriverExitFragment()).commit()
                    true
                }

                else -> false
            }
        }

        // 앱 초기 실행 시 홈화면으로 설정
        if (binding.bottomNavigationView.selectedItemId == 0) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_maps
        }
    }
}
