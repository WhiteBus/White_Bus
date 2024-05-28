package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.databinding.FragmentWaitingViBinding
import com.google.mediapipe.examples.facelandmarker.remote.adapter.PassengerAdapter
import com.google.mediapipe.examples.facelandmarker.remote.adapter.User
import com.google.mediapipe.examples.facelandmarker.remote.adapter.UserAdapter

class WaitingViFragment : Fragment() {

    companion object {
        private const val ARG_USERS = "users"

        fun newInstance(users: ArrayList<User>): WaitingViFragment {
            val fragment = WaitingViFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_USERS, users)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val users: List<User> = arguments?.getParcelableArrayList(ARG_USERS) ?: emptyList()
        userAdapter = UserAdapter(users)
        recyclerView.adapter = userAdapter

        return view
    }
}
