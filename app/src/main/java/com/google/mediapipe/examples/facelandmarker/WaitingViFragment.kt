package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentWaitingViBinding
import com.google.mediapipe.examples.facelandmarker.remote.adapter.PassengerAdapter

class WaitingViFragment : Fragment(){
    private lateinit var binding : FragmentWaitingViBinding
    private lateinit var adapter: PassengerAdapter
    // private val repository = PassengerRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWaitingViBinding.inflate(inflater, container, false)

        // recyclerView()
        // observePassengers()

        return binding.root
    }

    private fun recyclerView() {
        binding.profileNumberOfWaitingViRv.layoutManager = LinearLayoutManager(requireContext())
        // adapter 에 List를 넣어줌
        adapter = PassengerAdapter(emptyList())
        binding.profileNumberOfWaitingViRv.adapter = adapter
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

    }
}