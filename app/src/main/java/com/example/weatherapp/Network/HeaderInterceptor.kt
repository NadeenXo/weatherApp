package com.example.weatherapp.Network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .build()

        return chain.proceed(request)
    }
}