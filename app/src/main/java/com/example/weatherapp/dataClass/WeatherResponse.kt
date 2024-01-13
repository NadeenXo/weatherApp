package com.example.weatherapp.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastEntry>,
    val city: City
) : Parcelable

@Parcelize
data class ForecastEntry(
    val dt: Long,
    val main: MainDetails,
    val weather: List<WeatherDetails>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain?,
    val sys: Sys,
    val dt_txt: String
) : Parcelable

@Parcelize
data class MainDetails(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
) : Parcelable

@Parcelize
data class WeatherDetails(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize

data class Clouds(val all: Int) : Parcelable

@Parcelize

data class Wind(val speed: Double, val deg: Int, val gust: Double) : Parcelable

@Parcelize

data class Rain(val `3h`: Double) : Parcelable
@Parcelize

data class Sys(val pod: String) : Parcelable

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
) : Parcelable

@Parcelize

data class Coord(val lat: Double, val lon: Double) : Parcelable
