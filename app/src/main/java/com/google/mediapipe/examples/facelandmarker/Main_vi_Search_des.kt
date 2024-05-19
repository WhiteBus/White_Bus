package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.Adapter.SearchStationAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationInfo
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationResponse
import com.google.mediapipe.examples.facelandmarker.remote.service.SearchStationService
import com.google.mediapipe.examples.facelandmarker.remote.view.SearchStationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OnItemClickListener {
    fun onItemClick(station: SearchStationInfo)
}

class Main_vi_Search_des : AppCompatActivity(), SearchStationView {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchStationAdapter: SearchStationAdapter
    private lateinit var searchStationService: SearchStationService
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vi_search_des)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.vi_drawer_rv)
        searchStationAdapter = SearchStationAdapter(itemClickListener = this)
        recyclerView.adapter = searchStationAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // SearchStationService 초기화
        searchStationService = SearchStationService()
        searchStationService.setSearchStationView(this)

        // EditText 초기화
        searchEditText = findViewById(R.id.iv_main_search_place_et)

        // EditText에서 텍스트가 변경될 때마다 즉시 검색 수행
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                performSearch()
            }
        })
    }
    private fun performSearch() {
        val lang = "0" // 언어 코드
        val stationName = searchEditText.text.toString() // EditText에서 검색할 정류장 이름 가져오기
        val stationClass = 1 // 역 종류
        val apiKey = "rfSg7BEmSQsZFsPbMswlSOp5iiDu6smXQXY56n+aR4U" // API 키
        searchStationService.getSearchStation(lang, stationName, stationClass, apiKey)
    }

    override fun onSearchStationSuccess(response: SearchStationResponse) {
        response.result?.let { result ->
            result.station?.let { stationList ->
                searchStationAdapter.setStationList(stationList)
            } ?: kotlin.run {
                // Handle case where station list is null
                Log.e("Main_vi_Search_des", "Station list is null")
            }
        } ?: kotlin.run {
            // Handle case where result is null
            Log.e("Main_vi_Search_des", "Search result is null")
        }
    }

    override fun onSearchStationFailure(errorMessage: String) {
        // 서버 응답 실패 시 처리
        // 예시로 실패 메시지를 출력합니다.
        println(errorMessage)
    }
    fun onItemClick(station: SearchStationInfo) {
        GlobalValues_end.endPointStation = station
        // 클릭된 항목의 정보를 로그로 출력
        println("--Station Info--")
        println("stationName: ${station.stationName}")
        println("stationID: ${station.stationID}")
        println("x: ${station.x}")
        println("y: ${station.y}")
        val intent = Intent(this, Main_searchPubPathT::class.java)
        startActivity(intent)
    }

    object GlobalValues_end {
        var endPointStation: SearchStationInfo? = null
    }
}
