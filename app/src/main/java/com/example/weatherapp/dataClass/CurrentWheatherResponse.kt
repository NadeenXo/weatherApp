package com.example.weatherapp.dataClass

data class CurrentWeatherResponse(
    val crood: CurrentWeatherCrood,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: CurrentWeatherWind?,
    val rain: CurrentWeatherRain?,
    val clouds: CurrentWeatherClouds?,
    val dt: Long,
    val sys: CurrentWeatherSys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class CurrentWeatherCrood(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

data class CurrentWeatherWind(
    val speed: Double?,
    val deg: Int,
    val gust: Double
)


data class CurrentWeatherRain(
    val `1h`: Double
)

data class CurrentWeatherClouds(
    val all: Int
)

data class CurrentWeatherSys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
