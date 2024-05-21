package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentDriverMapsBinding

class DriverMapsFragment : Fragment() {

    private lateinit var binding: FragmentDriverMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverMapsBinding.inflate(layoutInflater)


        return binding.root
    }

}