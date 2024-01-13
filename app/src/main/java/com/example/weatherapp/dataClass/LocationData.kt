package com.example.weatherapp.dataClass

data class LocationData(
    val name: String,
    val local_names: Map<String, String>,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
)

