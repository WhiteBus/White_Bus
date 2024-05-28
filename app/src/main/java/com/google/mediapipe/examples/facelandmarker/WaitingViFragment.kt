package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentWaitingViBinding
import com.google.mediapipe.examples.facelandmarker.remote.adapter.PassengerAdapter
import com.google.mediapipe.examples.facelandmarker.remote.adapter.UserAdapter
import kotlinx.coroutines.launch

class WaitingViFragment : Fragment() {
    private lateinit var binding : FragmentWaitingViBinding
    private lateinit var adapter: PassengerAdapter
    // private val repository = PassengerRepository()
    companion object {
        private const val ARG_NICLIST = "niclist"
        private const val ARG_IMAGELIST = "imagelist"

        fun newInstance(niclist: ArrayList<String>, imagelist: ArrayList<String>): WaitingViFragment {
            val fragment = WaitingViFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_NICLIST, niclist)
            args.putStringArrayList(ARG_IMAGELIST, imagelist)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var niclist: List<String>
    private lateinit var imagelist: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            niclist = it.getStringArrayList(ARG_NICLIST) ?: emptyList()
            imagelist = it.getStringArrayList(ARG_IMAGELIST) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_waiting_vi, container, false)

        // Initialize RecyclerView here
        val recyclerView: RecyclerView = view.findViewById(R.id.profile_number_of_waiting_vi_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = UserAdapter(niclist, imagelist)

        return view
    }



    private fun recyclerView() {
        binding.profileNumberOfWaitingViRv.layoutManager = LinearLayoutManager(requireContext())
        // adapter 에 List를 넣어줌
        adapter = PassengerAdapter(emptyList())
        binding.profileNumberOfWaitingViRv.adapter = adapter
    }

    private fun observePassengers() {
//         //파이어베이스 작동 이후 적용
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
//
    }
}