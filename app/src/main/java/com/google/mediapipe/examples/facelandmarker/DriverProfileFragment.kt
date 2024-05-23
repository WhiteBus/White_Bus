package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentDriverProfileBinding
import com.google.mediapipe.examples.facelandmarker.remote.adapter.PassengerAdapter
import com.google.mediapipe.examples.facelandmarker.remote.repository.PassengerRepository
import kotlinx.coroutines.launch

class DriverProfileFragment : Fragment(){

    private lateinit var binding : FragmentDriverProfileBinding
    private lateinit var adapter: PassengerAdapter
    // private val repository = PassengerRepository()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDriverProfileBinding.inflate(inflater, container, false)

        // recyclerView()
        // observePassengers()

        return binding.root
    }

    private fun recyclerView() {
        binding.profileNumberOfPassengerRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = PassengerAdapter(emptyList())
        binding.profileNumberOfPassengerRv.adapter = adapter
    }

    private fun observePassengers() {
        // 파이어베이스 작동 이후 적용
//        lifecycleScope.launch {
//            val passengers = repository.getPassengers()
//            adapter = PassengerAdapter(passengers)
//            binding.profileNumberOfPassengerRv.adapter = adapter
//        }
//
//        repository.listenToPassengers { passengers ->
//            adapter = PassengerAdapter(passengers)
//            binding.profileNumberOfPassengerRv.adapter = adapter
//        }

        // 하차벨 눌렀을 때 눌렸다는 빨간색 원 표시 (visibility)
    }
}