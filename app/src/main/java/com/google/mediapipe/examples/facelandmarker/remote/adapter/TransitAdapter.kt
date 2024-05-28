package com.google.mediapipe.examples.facelandmarker.remote.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mediapipe.examples.facelandmarker.OnStationUser
import com.google.mediapipe.examples.facelandmarker.R
import com.google.mediapipe.examples.facelandmarker.remote.dto.TransitInfo

class TransitAdapter(private var transitList: List<TransitInfo>) :
    RecyclerView.Adapter<TransitAdapter.TransitViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var stationid: String = ""
    private var stationX: String = ""
    private var stationY: String = ""

    class TransitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val borderBoxDes: MaterialButton = view.findViewById(R.id.borderBox_des)
        val transitInfo: TextView = view.findViewById(R.id.transit_info)
        val transitInfoStartName: TextView = view.findViewById(R.id.transit_info_startName)
        val transitInfoEndName: TextView = view.findViewById(R.id.transit_info_endName)
        val sectionDistance: TextView = view.findViewById(R.id.section_distance)
        val sectionTime: TextView = view.findViewById(R.id.section_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bus_path, parent, false)
        return TransitViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransitViewHolder, position: Int) {
        val transitInfo = transitList[position]
        holder.transitInfo.text = transitInfo.busNo
        holder.transitInfoStartName.text = transitInfo.startName
        holder.transitInfoEndName.text = transitInfo.endName
        holder.sectionDistance.text = "${transitInfo.distance} km"
        holder.sectionTime.text = "${transitInfo.sectionTime} min"

        auth = FirebaseAuth.getInstance()

        val userid = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("BlindUser").document(userid)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    data?.let {
                        stationid = it["startStationID"] as? String ?: ""
                        stationX = it["startStationX"] as? String ?: ""
                        stationY = it["startStationY"] as? String ?: ""
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
            // Add station data to the intent
            intent.putExtra("busNo", transitInfo.busNo)
            intent.putExtra("stationID", stationid)
            intent.putExtra("stationX", stationX)
            intent.putExtra("stationY", stationY)
            // Start the activity using the context from the view
            it.context.startActivity(intent)

            println("버튼 눌렸니")
        }
    }

    override fun getItemCount() = transitList.size

    fun updateData(newTransitList: List<TransitInfo>) {
        transitList = newTransitList
        notifyDataSetChanged()
    }
}