package com.google.mediapipe.examples.facelandmarker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.integrity.internal.o
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
import kotlin.collections.ArrayList

class DriverMainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityDriverMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val marker = Marker()
    private lateinit var mediaPlayer: MediaPlayer
    private val handler = Handler(Looper.getMainLooper())
    private var isWarningShowing = false
    private var overlayLayout: ViewGroup? = null
    var stationidset = HashSet<String>()
    var mybusnum: String= ""
    var coordlist = ArrayList<Pair<Double, Double>>()
    val stationid: String = ""
    var sidlis: List<String> = emptyList<String>()
    var coordset = HashSet<Pair<String, String>>()
    var latset = HashSet<Double>()
    var lonset = HashSet<Double>()
    var latlist: List<Double> = emptyList<Double>()
    var lonlist: List<Double> = emptyList<Double>()
    var nicset = HashSet<String>()
    var imageset = HashSet<String>()
    var niclist: List<String> = emptyList<String>()
    var imagelist: List<String> = emptyList<String>()
    var latblindlist: List<String> = emptyList<String>()
    var lonblindlist: List<String> = emptyList<String>()





    private val statusChecker = object : Runnable {
        override fun run() {
            checkStatusTextView()
            handler.postDelayed(this, 1000) // 1초마다 실행
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        // MediaPlayer 초기화
        mediaPlayer = MediaPlayer.create(this, R.raw.wistle_long)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        handler.post(statusChecker) // Runnable 시작
        fetchStationsAndAddMarkers()
    }

    private fun checkStatusTextView() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null && fragment.view != null) {
            val statusTextView = fragment.view?.findViewById<TextView>(R.id.statusTextView)
            if (statusTextView != null && statusTextView.text != null) {
                val statusText = statusTextView.text.toString()
                if (statusText != "awake" && !isWarningShowing) {
                    showWarningOverlay()
                }
            }
        } else {
            Toast.makeText(this, "statusTextView를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWarningOverlay() {
        isWarningShowing = true
        mediaPlayer.start() // 경고음 재생

        overlayLayout = layoutInflater.inflate(R.layout.sleepnot, null) as ViewGroup
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addContentView(overlayLayout, params)

        overlayLayout?.setOnClickListener {
            removeWarningOverlay()
        }

        // 3초 후에 오버레이 제거
        handler.postDelayed({
            removeWarningOverlay()
        }, 3000)
    }

    private fun removeWarningOverlay() {
        overlayLayout?.let {
            (it.parent as? ViewGroup)?.removeView(it)
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare()
        }
        isWarningShowing = false
        overlayLayout = null
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

//        naverMap.setOnMapClickListener { _, coord ->
//            addMarkerToMap(coord.latitude, coord.longitude)
//        }

        // 초기 마커 추가 (서울의 좌표)
        //addMarkerToMap(37.5665, 126.9780)
    }

    private fun fetchStationsAndAddMarkers() {
        auth = FirebaseAuth.getInstance()


        Log.w("Tag", "call abcd")
        val uid = auth.uid

        db.collection("DriverUser").document(uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        mybusnum = (it["busNumb"] as? String).toString()
                        Log.w("Tag", "abcd pickun: ${mybusnum}")

                        // mybusnum 값이 설정된 후 BlindUser 쿼리를 실행합니다.
                        db.collection("BlindUser")
                            .whereEqualTo("pickupNum", mybusnum)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    for (document in documents) {
                                        val stationid = document.data["stationId"].toString()
                                        val latitude = document.data["latitude"].toString()
                                        val longitude = document.data["longitude"].toString()

                                        Log.w("Tag", "abcd stationid: ${stationid}")
                                        stationidset.add(stationid)
                                        latblindlist += latitude
                                        lonblindlist += longitude

                                    }
                                    sidlis = stationidset.toList() // Set을 다시 List로 변환
                                    fire(sidlis)
                                    Log.w("Tag", "abcd idlist: ${sidlis}")
                                } else {
                                    Log.w("Tag", "abcd: No matching documents found.")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "abcd: Error getting documents: $exception", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.w("Tag", "abcd busnum fail")
                    Log.w("Tag", "abcd pickun: ${uid}")
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    fun fire(sidlis: List<String>) {
        for (i in sidlis) {
            Log.w("Tag", "abcd sid: ${i}i")

            db.collection("OnStation").document(i).collection("stayUser")
                .whereEqualTo("pickupNum", mybusnum)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        try {
                            val latitude = document.get("latitude")
                            val longitude = document.get("longitude")
                            val nick = document.get("nickname").toString()
                            val imageurl = document.get("profileImageUrl").toString()
                            Log.w("Tag", "abcd userinfo: ${nick}, ${imageurl}")

                            // Check if latitude and longitude are of type Number
                            val latDouble = when (latitude) {
                                is Number -> latitude.toDouble()
                                is String -> latitude.toDoubleOrNull()
                                else -> null
                            }

                            val lonDouble = when (longitude) {
                                is Number -> longitude.toDouble()
                                is String -> longitude.toDoubleOrNull()
                                else -> null
                            }

                            if (latDouble != null && lonDouble != null) {
                                Log.w("Tag", "abcd coord: ${latDouble}, ${lonDouble}")
                                latset.add(latDouble)
                                lonset.add(lonDouble)

                            } else {
                                Log.w("Tag", "abcd: Invalid coordinates found")
                            }

                            latlist = latset.toList()
                            lonlist = lonset.toList()
                            niclist += nick
                            imagelist += imageurl
                            for(i in 0 until niclist.size) {
                                Log.w("Tag", "abcd coord: ${niclist}, ${imagelist}")
                            }
                            if (latitude != null && longitude != null) {
                                Log.w("Tag", "abcd: Call addMarkerToMap2")
                                if (latDouble != null) {
                                    if (lonDouble != null) {
                                        addMarkerToMap(latDouble, lonDouble, mybusnum, niclist, imagelist)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("Tag", "abcd: Error parsing document fields", e)
                        }
                    }

//                    for (i in 0 until latlist.size) {
//                        Log.w("Tag", "abcd: Call addMarkerToMap: ${latlist[i]}, ${lonlist[i]}")
//                        if (latlist[i] != null && lonlist[i] != null) {
//                            Log.w("Tag", "abcd: Call addMarkerToMap2")
//                            addMarkerToMap(latlist[i], lonlist[i], mybusnum, niclist, imagelist)
//                        }
//                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Tag", "abcd: Error getting documents: ", exception)
                    Toast.makeText(this, "abcd: Error getting documents: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun addMarkerToMap(lat: Double, lng: Double, mybusnum: String, niclist: List<String>, imagelist: List<String>) {
        val marker = Marker()
        marker.position = LatLng(lat, lng)
        marker.icon = OverlayImage.fromResource(R.drawable.people) // 커스텀 아이콘 설정
        marker.map = naverMap
        Log.w("Tag", "abcd: ${lat}, ${lng}")


        marker.setOnClickListener {
            // 마커 클릭 시 다이얼로그 표시
            showMarkerDialog(mybusnum, niclist, imagelist)
            true
        }

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng))
        naverMap.moveCamera(cameraUpdate)
        naverMap.moveCamera(CameraUpdate.zoomTo(18.0))

        getAddress(lat, lng)
    }

    private fun showMarkerDialog(mybusnum: String, niclist: List<String>, imagelist: List<String>) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirmlist, null)
        builder.setView(dialogView)

        val noButton: Button = dialogView.findViewById(R.id.noButton)
        val yesButton: Button = dialogView.findViewById(R.id.yesButton)

        val dialog = builder.create()
        dialog.show()

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        yesButton.setOnClickListener {
            val fragment = WaitingViFragment.newInstance(ArrayList(niclist), ArrayList(imagelist))
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
            dialog.dismiss()
        }
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
