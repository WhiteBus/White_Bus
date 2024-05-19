package com.google.mediapipe.examples.facelandmarker

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.odsay.com/v1/api/"

object RetrofitModule {
    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    fun getRetrofit(): Retrofit {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.odsay.com/v1/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }
}
