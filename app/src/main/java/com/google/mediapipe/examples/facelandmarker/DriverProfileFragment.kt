package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentDriverProfileBinding
import com.google.mediapipe.examples.facelandmarker.remote.adapter.PassengerAdapter
import com.google.mediapipe.examples.facelandmarker.remote.adapter.UserAdapter
import kotlinx.coroutines.launch

class DriverProfileFragment : Fragment() {
    private lateinit var binding : FragmentDriverProfileBinding
    private lateinit var adapter: PassengerAdapter
    // private val repository = PassengerRepository()
    companion object {
        private const val ARG_NICBUSLIST = "nicbuslist"
        private const val ARG_IMAGEBUSLIST = "imagebuslist"

        fun newInstance(nicbuslist: ArrayList<String>, imagebuslist: ArrayList<String>): DriverProfileFragment {
            val fragment = DriverProfileFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_NICBUSLIST, nicbuslist)
            args.putStringArrayList(ARG_IMAGEBUSLIST, imagebuslist)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var nicbuslist: List<String>
    private lateinit var imagebuslist: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nicbuslist = it.getStringArrayList(ARG_NICBUSLIST) ?: emptyList()
            imagebuslist = it.getStringArrayList(ARG_IMAGEBUSLIST) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_driver_profile, container, false)

        // Initialize RecyclerView here
        val recyclerView: RecyclerView = view.findViewById(R.id.profile_number_of_passenger_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)
        Log.w("HI", "abcde: ${nicbuslist}, ${imagebuslist}")

        recyclerView.adapter = UserAdapter(nicbuslist, imagebuslist)

        return view
    }



//    private fun recyclerView() {
//        binding.profileNumberOfWaitingViRv.layoutManager = LinearLayoutManager(requireContext())
//        // adapter 에 List를 넣어줌
//        adapter = PassengerAdapter(emptyList())
//        binding.profileNumberOfWaitingViRv.adapter = adapter
//    }
//
//    private fun observePassengers() {
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
//}
}