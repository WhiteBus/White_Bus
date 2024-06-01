package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.remote.adapter.SearchStationAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationInfo
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationResponse
import com.google.mediapipe.examples.facelandmarker.remote.service.SearchStationService
import com.google.mediapipe.examples.facelandmarker.remote.view.SearchStationView
import java.util.Locale

class Main_vi_Search_des : AppCompatActivity(), SearchStationView {

    private lateinit var tts: TextToSpeech

    private lateinit var searchStationAdapter: SearchStationAdapter
    private lateinit var searchStationService: SearchStationService
    private lateinit var viDrawerRv: RecyclerView
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        // TextToSpeech 초기화
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.KOREAN
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vi_search_des)

        viDrawerRv = findViewById(R.id.vi_drawer_rv)
        searchEditText = findViewById(R.id.iv_main_search_place_et)

        searchStationAdapter = SearchStationAdapter { station -> onItemClick(station) }
        viDrawerRv.layoutManager = LinearLayoutManager(this)
        viDrawerRv.adapter = searchStationAdapter

        searchStationService = SearchStationService()
        searchStationService.setSearchStationView(this)

        // 인텐트로 전달된 음성 인식 결과를 받아서 설정
        val recognizedText = intent.getStringExtra("recognizedText")
        if (recognizedText != null) {
            searchEditText.setText(recognizedText)
            performSearch()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                performSearch()
            }
        })
        searchEditText.setOnLongClickListener {
            speak("이동할 장소를 입력해주세요")
            true
        }
    }
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    private fun performSearch() {
        val lang = "0"
        val stationName = searchEditText.text.toString()
        val stationClass = 1 //버스정류장
        val apiKey = "AK/JO+KzScwJZFaoH4SuBi602qUO+wx2rbjRsJfLfhE"
        searchStationService.getSearchStation(lang, stationName, stationClass, apiKey)
    }

    override fun onSearchStationSuccess(response: SearchStationResponse) {
        response.result?.station?.let {
            searchStationAdapter.setStationList(it)
        } ?: run {
            Log.e("Main_vi_Search_des", "Station list is null")
        }
    }

    override fun onSearchStationFailure(errorMessage: String) {
        println(errorMessage)
    }

    fun onItemClick(station: SearchStationInfo) {
        GlobalValues_last.lastPointStation = station
        // 클릭된 항목의 정보를 로그로 출력
        println("--Station Info--")
        println("Des_stationName: ${station.stationName}")
        println("Des_stationID: ${station.stationID}")
        println("Des_station_x: ${station.x}")
        println("Des_station_y: ${station.y}")
        val intent = Intent(this, searchPubPathT::class.java)
        intent.putExtra("laststationname", station.stationName) // 선택된 역 이름을 전달
        startActivity(intent)
    }

    object GlobalValues_last {
        var lastPointStation: SearchStationInfo? = null
        //var stationName: String? = null
    }
}