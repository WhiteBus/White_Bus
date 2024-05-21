package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.facelandmarker.database.DestinationContract
import com.google.mediapipe.examples.facelandmarker.repository.DestinationRepository

class activity_vi_search_des : AppCompatActivity() {

    private lateinit var destinationRepository: DestinationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vi_search_des)

        destinationRepository = DestinationRepository(this)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)

        val cursor = destinationRepository.getAllDestinations()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("_id"))
                val name = getString(getColumnIndexOrThrow(DestinationContract.DestinationEntry.COLUMN_NAME_NAME))
                when (id) {
                    1L -> button1.text = name
                    2L -> button2.text = name
                    3L -> button3.text = name
                }
            }
        }
        cursor.close()
    }

    override fun onDestroy() {
        destinationRepository.close()
        super.onDestroy()
    }
}
