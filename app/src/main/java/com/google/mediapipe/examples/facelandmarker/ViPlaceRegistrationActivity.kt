package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityViPlaceRegistrationBinding
import com.google.mediapipe.examples.facelandmarker.remote.adapter.SearchStationAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationInfo
import com.google.mediapipe.examples.facelandmarker.remote.dto.SearchStationResponse
import com.google.mediapipe.examples.facelandmarker.remote.service.SearchStationService
import com.google.mediapipe.examples.facelandmarker.remote.view.SearchStationView
import com.google.mediapipe.examples.facelandmarker.repository.DestinationRepository
import com.google.mediapipe.examples.facelandmarker.repository.activity_home
import java.util.*

class ViPlaceRegistrationActivity : AppCompatActivity(), TextToSpeech.OnInitListener, SearchStationView {

    private lateinit var searchStationAdapter: SearchStationAdapter
    private lateinit var searchStationService: SearchStationService
    private lateinit var destinationRepository: DestinationRepository
    private lateinit var binding: ActivityViPlaceRegistrationBinding
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var nicknameSearchBar: EditText
    private lateinit var placeSearchBar: EditText
    private var firstStation: SearchStationInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViPlaceRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nicknameSearchBar = findViewById(R.id.iv_main_set_nickname_et)
        placeSearchBar = findViewById(R.id.iv_main_set_place_et)
        destinationRepository = DestinationRepository(this)
        textToSpeech = TextToSpeech(this, this)

        searchStationAdapter = SearchStationAdapter { station ->
            onItemClick(station)
        }

        // Initialize searchStationService and set this activity as the view
        searchStationService = SearchStationService()
        searchStationService.setSearchStationView(this)

        // 음성 인식
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        // 닉네임 음성 버튼이 눌렸을 때
        val nicknameSpeechBtn: ImageView = findViewById(R.id.iv_main_nickname_record_iv)
        nicknameSpeechBtn.setOnClickListener {
            nicknameSpeechRecognition()
        }
        // 장소 음성 버튼이 눌렸을 때
        val placeSpeechBtn: ImageView = findViewById(R.id.iv_main_place_iv)
        placeSpeechBtn.setOnClickListener {
            placeSpeechRecognition()
        }

        // 이전 버튼이 눌렸을 때
        val backButton: ImageView = findViewById(R.id.iv_registration_back_iv)
        backButton.setOnClickListener {
            val intent = Intent(this, activity_home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        // 검색 버튼
        val searchButton: Button = findViewById(R.id.iv_main_search_iv)
        searchButton.setOnClickListener {
            performSearch()
        }
        // 등록 버튼
        val registerButton: Button = findViewById(R.id.iv_registration_register_iv)
        registerButton.setOnClickListener {
            registerStation()
        }
    }

    private fun performSearch() {
        val lang = "0"
        val stationName = placeSearchBar.text.toString()
        val stationClass = 1
        val apiKey = "t3zmnsSHmjzeGx9ruZeKGAcT0uLFJn7tlTyjZVc0Y/g"
        searchStationService.getSearchStation(lang, stationName, stationClass, apiKey)
        Log.d("searchStationService", searchStationService.toString())
        speak("검색이 완료되었습니다.")
    }

    override fun onSearchStationSuccess(response: SearchStationResponse) {
        response.result?.station?.let { stationList ->
            if (stationList.isNotEmpty()) {
                firstStation = stationList[0]
                Log.d("SearchStation", "First Station - Name: ${firstStation?.stationName}, x: ${firstStation?.x}, y: ${firstStation?.y}")
                searchStationAdapter.setStationList(stationList)
            } else {
                firstStation = null
                Log.e("SearchStation", "Station list is empty")
            }
        } ?: run {
            firstStation = null
            Log.e("SearchStation", "Station list is null")
        }
    }

    override fun onSearchStationFailure(errorMessage: String) {
        firstStation = null
        Log.e("SearchStation", errorMessage)
        speak("장소 이름을 다시 설정해주세요")
    }

    private fun registerStation() {
        val nickname = nicknameSearchBar.text.toString()
        if (firstStation != null) {
            val stationName = firstStation!!.stationName
            val x = firstStation!!.x
            val y = firstStation!!.y
            destinationRepository.insertDestination(nickname, x, y, stationName)
            Log.d("RegisterStation", "Registered Station - Name: $stationName, x: $x, y: $y, Nickname: $nickname")
            speak("장소가 등록되었습니다.")
        } else {
            speak("장소 이름을 다시 설정해주세요")
            Log.e("RegisterStation", "No station to register")
        }
    }

    private fun nicknameSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요")
        startActivityForResult(intent, NICKNAME_SPEECH_REQUEST_CODE)
    }

    private fun placeSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "장소를 입력하세요")
        startActivityForResult(intent, PLACE_SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null && results.isNotEmpty()) {
                val recognizedText = results[0]
                when (requestCode) {
                    NICKNAME_SPEECH_REQUEST_CODE -> nicknameSearchBar.setText(recognizedText)
                    PLACE_SPEECH_REQUEST_CODE -> placeSearchBar.setText(recognizedText)
                }
            }
        }
    }

    private fun onItemClick(station: SearchStationInfo) {
        firstStation = station
        Log.d("SearchStation", "Clicked Station - Name: ${station.stationName}, x: ${station.x}, y: ${station.y}")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language is not supported")
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed")
        }
    }

    private fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        speechRecognizer.destroy()
        destinationRepository.close()
        super.onDestroy()
    }

    companion object {
        private const val NICKNAME_SPEECH_REQUEST_CODE = 123
        private const val PLACE_SPEECH_REQUEST_CODE = 456
    }
}
