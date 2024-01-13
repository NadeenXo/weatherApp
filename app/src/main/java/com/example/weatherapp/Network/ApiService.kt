package com.example.weatherapp.Network

import com.example.weatherapp.dataClass.CurrentWeatherResponse
import com.example.weatherapp.dataClass.LocationData
import com.example.weatherapp.dataClass.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //https://api.openweathermap.org/data/2.5/forecast?lat=30.04700345245006&lon=31.38603182647413&units=metric&appid=b82e30ca6c222e7f12452215dfbcdf36


    //https://api.openweathermap.org/?q=Cairo,,EG&limit=5&appid=b82e30ca6c222e7f12452215dfbcdf36
    @GET("geo/1.0/direct")
    fun getLocation(
        @Query("q") location: String = "Cairo,,EG",
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String = "b82e30ca6c222e7f12452215dfbcdf36"
    ): Call<List<LocationData>>

    //api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid={API key}
    @GET("/data/2.5/forecast")
    fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String="metric",
        @Query("appid") apiKey: String = "b82e30ca6c222e7f12452215dfbcdf36"
    ): Call<WeatherResponse>

    @GET("/data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String="metric",
        @Query("appid") apiKey: String = "b82e30ca6c222e7f12452215dfbcdf36"
    ): Call<CurrentWeatherResponse>

}