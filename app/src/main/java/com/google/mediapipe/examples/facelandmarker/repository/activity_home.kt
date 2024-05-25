package com.google.mediapipe.examples.facelandmarker.repository

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.Main_vi_Search_des
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.ViPlaceRegistrationActivity
import com.google.mediapipe.examples.facelandmarker.database.DestinationContract
import com.google.mediapipe.examples.facelandmarker.favorite.DestinationAdapter

class activity_home : AppCompatActivity() {

    private lateinit var destinationRepository: DestinationRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DestinationAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var hamburgerIcon: ImageView
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var searchBar: EditText

    val button1: TextView by lazy { findViewById(R.id.button1_text1) }
    val button2: TextView by lazy { findViewById(R.id.button2_text1) }
    val button3: TextView by lazy { findViewById(R.id.button3_text1) }

    val button1_1: TextView by lazy { findViewById(R.id.button1_text2) }
    val button2_1: TextView by lazy { findViewById(R.id.button2_text2) }
    val button3_1: TextView by lazy { findViewById(R.id.button3_text2) }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        destinationRepository = DestinationRepository(this)

        val button4Container: ConstraintLayout = findViewById(R.id.button4_container)

        searchBar = findViewById(R.id.iv_main_search_place_et)

        destinationRepository = DestinationRepository(this)
        recyclerView = findViewById(R.id.recyclerViewDestinations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DestinationAdapter(destinationRepository.getAllDestinations()) { name ->
            onDestinationItemClick(name)
        }
        recyclerView.adapter = adapter

        val cursor = destinationRepository.getAllDestinations()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("_id"))
                val name = getString(getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_NAME))
                val address = getString(getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_ADDRESS))
                when (id) {
                    1L -> button1.text = name
                    2L -> button2.text = name
                    3L -> button3.text = name
                }
                when (id) {
                    1L -> button1_1.text = address
                    2L -> button2_1.text = address
                    3L -> button3_1.text = address
                }
            }
        }
        cursor.close()
        drawerLayout = findViewById(R.id.drawer_layout)
        hamburgerIcon = findViewById(R.id.iv_main_hamburger_iv)
        recyclerView = findViewById(R.id.recyclerViewDestinations)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 햄버거 아이콘 클릭 시 DrawerLayout 열기
        hamburgerIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 음성인식 초기화
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechButton: ImageView = findViewById(R.id.btnSpeech)
        speechButton.setOnClickListener {
            startSpeechRecognition()
        }

        // button4_container 클릭 시 ViPlaceRegistrationActivity로 이동
        button4Container.setOnClickListener {
            val intent = Intent(this, ViPlaceRegistrationActivity::class.java)
            startActivity(intent)
        }
        // 목적지 검색창으로 이동
        clickListener()
    }

    private fun onDestinationItemClick(name: String) {
        val coordinates = destinationRepository.getDestinationCoordinatesByName(name)
        // 좌표를 활용한 처리
        coordinates?.let {
            // 예: 지도 화면으로 이동 또는 다른 액티비티로 이동
            val intent = Intent(this, Main_vi_Search_des::class.java)
            intent.putExtra("xCoord", it.first)
            intent.putExtra("yCoord", it.second)
            startActivity(intent)
        }
    }
//  즐겨찾기 검색기능 여기다 추가!
    private fun clickLitener1(){
        searchBar.setOnClickListener {
            val coor = destinationRepository.getDestinationCoordinatesByName(button1.text.toString())
            switchActivity(Main_vi_Search_des::class.java)
        }
    }

    private fun clickLitener2(){
        searchBar.setOnClickListener {
            val coor = destinationRepository.getDestinationCoordinatesByName(button2.text.toString())
            switchActivity(Main_vi_Search_des::class.java)
        }
    }

    private fun clickLitener3(){
        searchBar.setOnClickListener {
            val coor = destinationRepository.getDestinationCoordinatesByName(button3.text.toString())
            switchActivity(Main_vi_Search_des::class.java)
        }
    }

    private fun clickListener() {
        searchBar.setOnClickListener {
            switchActivity(Main_vi_Search_des::class.java)
        }
    }

    private fun switchActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요")
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onDestroy() {
        destinationRepository.close()
        speechRecognizer.destroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null && results.isNotEmpty()) {
                val recognizedText = results[0]
                // 여기에 인식된 텍스트를 원하는 EditText에 설정하는 코드 추가
                searchBar.setText(recognizedText)
            }
        }
    }

    companion object {
        private const val SPEECH_REQUEST_CODE = 123
    }
}
