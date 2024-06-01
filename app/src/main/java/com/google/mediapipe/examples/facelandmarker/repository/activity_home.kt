package com.google.mediapipe.examples.facelandmarker.repository

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.inputmethod.InputMethodManager
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
import com.google.mediapipe.examples.facelandmarker.searchPubPathT

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
        adapter = DestinationAdapter(destinationRepository.getAllDestinations()) { name, address ->
            onDestinationItemClick(name, address)
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

        // 즐겨찾기 버튼 클릭 리스너 추가
        button1.setOnClickListener { onFavoriteButtonClick(button1.text.toString()) }
        button2.setOnClickListener { onFavoriteButtonClick(button2.text.toString()) }
        button3.setOnClickListener { onFavoriteButtonClick(button3.text.toString()) }
    }

    // 즐겨찾기 검색 기능 추가
    private fun onFavoriteButtonClick(favoriteName: String) {
        val coordinates = destinationRepository.getDestinationCoordinatesByName(favoriteName)
        coordinates?.let {
            val intent = Intent(this, searchPubPathT::class.java)
            intent.putExtra("endStationX", it.first)
            intent.putExtra("endStationY", it.second)
            // address 값을 laststationname에 전달
                intent.putExtra("laststationname", favoriteName)
            startActivity(intent)
        }
        println("Destination's x,y : $coordinates")
    }

    private fun onDestinationItemClick(name: String, address: String) {
        val coordinates = destinationRepository.getDestinationCoordinatesByName(name)
        coordinates?.let {
            val intent = Intent(this, Main_vi_Search_des::class.java)
            intent.putExtra("xCoord", it.first)
            intent.putExtra("yCoord", it.second)
            intent.putExtra("laststationname", address) // address 값을 전달
            startActivity(intent)
        }
    }

    private fun clickListener() {
        searchBar.setOnClickListener {
            // 키보드를 숨깁니다
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchBar.windowToken, 0)

            // 다음 Activity로 이동합니다
            val intent = Intent(this, Main_vi_Search_des::class.java)
            startActivity(intent)
        }
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
                // 인식된 텍스트를 Main_vi_Search_des로 전달
                val intent = Intent(this, Main_vi_Search_des::class.java)
                intent.putExtra("recognizedText", recognizedText)
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val SPEECH_REQUEST_CODE = 123
    }
}