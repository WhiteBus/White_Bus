package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.BusNumberInputActivity
import com.google.mediapipe.examples.facelandmarker.OnStationUser
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.dto.TransitInfo

class TransitAdapter(private var transitList: List<TransitInfo>) :

    RecyclerView.Adapter<TransitAdapter.TransitViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var stationid:String = ""
    var stationX:String = ""
    var stationY:String = ""
    class TransitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val borderBoxDes: MaterialButton = view.findViewById(R.id.borderBox_des)
        val transitInfoStartName: TextView = view.findViewById(R.id.transit_info_startName)
        val transitInfoEndName: TextView = view.findViewById(R.id.transit_info_endName)
        val sectionDistance: TextView = view.findViewById(R.id.section_distance)
        val sectionTime: TextView = view.findViewById(R.id.section_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bus_path, parent, false)
        return TransitViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransitViewHolder, position: Int) {
        val transitInfo = transitList[position]
        holder.borderBoxDes.text = transitInfo.busNo
        holder.transitInfoStartName.text = transitInfo.startName
        holder.transitInfoEndName.text = transitInfo.endName
        holder.sectionDistance.text = "${transitInfo.distance} km"
        holder.sectionTime.text = "${transitInfo.sectionTime} min"

        auth = FirebaseAuth.getInstance()

        val uid = auth.uid
        val userid = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("BlindUser").document(userid)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        stationid = (it["startStationID"] as? String).toString()
                        stationX = (it["startStationX"] as? String).toString()
                        stationY = (it["startStationY"] as? String).toString()

                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
        holder.borderBoxDes.setOnClickListener {
            // Create an Intent to start the OnStationUser activity
            val intent = Intent(it.context, OnStationUser::class.java)
            // Start the activity using the context from the view
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount() = transitList.size

    fun updateData(newTransitList: List<TransitInfo>) {
        transitList = newTransitList
        notifyDataSetChanged()
    }
}