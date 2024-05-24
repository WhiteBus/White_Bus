package com.google.mediapipe.examples.facelandmarker

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityViPlaceRegistrationBinding

class ViPlaceRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViPlaceRegistrationBinding
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var nicknameSearchBar: EditText
    private lateinit var placeSearchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViPlaceRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nicknameSearchBar = findViewById(R.id.iv_main_set_nickname_et)
        placeSearchBar = findViewById(R.id.iv_main_set_place_et)

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
            finish() // activity_home 으로 돌아감
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

    companion object {
        private const val NICKNAME_SPEECH_REQUEST_CODE = 123
        private const val PLACE_SPEECH_REQUEST_CODE = 456
    }
}
