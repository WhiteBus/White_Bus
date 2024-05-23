package com.google.mediapipe.examples.facelandmarker

import DriverMapsFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityDriverMainBinding

class DriverMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_maps
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
    }
}