package com.google.mediapipe.examples.facelandmarker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.examples.facelandmarker.remote.adapter.BusArrivalAdapter
import com.google.mediapipe.examples.facelandmarker.remote.dto.RealtimeBusArrivalRes
import com.google.mediapipe.examples.facelandmarker.remote.retrofit.BusArrivalInterface

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Main_Bus_Arrival : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var busArrivalAdapter: BusArrivalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vi_bus_arrival)

        recyclerView = findViewById(R.id.rv_arrivalBus)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val busNumbers = intent.getStringArrayListExtra("busNumbers") ?: arrayListOf()

        val stationID = Main_NearestStation.GlobalValue_start.stationID

        if (stationID != null) {
            fetchBusArrivalData(stationID, busNumbers)
        } else {
            println("Station ID is not available.")
        }
    }

    private fun fetchBusArrivalData(stationID: Int, busNumbers: List<String>) {
        val retrofit = RetrofitModule.getRetrofit()
        val busArrivalService = retrofit.create(BusArrivalInterface::class.java)
        val call = busArrivalService.getRealtimeBusArrival(lang = 0, stationID = stationID, apiKey = "rfSg7BEmSQsZFsPbMswlSOp5iiDu6smXQXY56n+aR4U")

        call.enqueue(object : Callback<RealtimeBusArrivalRes> {
            override fun onResponse(call: Call<RealtimeBusArrivalRes>, response: Response<RealtimeBusArrivalRes>) {
                if (response.isSuccessful) {
                    val busArrivalResponse = response.body()
                    if (busArrivalResponse != null && busArrivalResponse.result != null) {
                        val busArrivalList = busArrivalResponse.result.real.filter { busNumbers.contains(it.routeNm) }
                        if (busArrivalList.isNotEmpty()) {
                            busArrivalAdapter = BusArrivalAdapter(busArrivalList)
                            recyclerView.adapter = busArrivalAdapter
                        } else {
                            println("No bus arrivals found.")
                        }
                    } else {
                        println("Response is null or result is null.")
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<RealtimeBusArrivalRes>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}