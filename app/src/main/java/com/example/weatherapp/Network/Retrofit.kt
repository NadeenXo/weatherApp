package com.example.weatherapp.Network

import com.example.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Retrofit {

    //    val is initialized only the first time the fun is called
    //    and the same instance is used every time you try to access the fun
    fun getInstance(): ApiService {
        return retrofitInstance
    }

    private val interceptor = HeaderInterceptor()
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofitInstance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}