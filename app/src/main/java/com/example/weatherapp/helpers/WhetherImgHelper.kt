package com.example.weatherapp.helpers

import com.example.weatherapp.R

class WhetherImgHelper {
    companion object {
        fun setWeatherImage(weatherDescription: String): Int {
            return when (weatherDescription.lowercase()) {
//            "clear" -> R.drawable.sunny
                "rain", "thunderstorm", "drizzle" -> R.drawable.rainy
                "wind" -> R.drawable.windy
                "Ash", "SandSand" -> R.drawable.wind
                "Tornado", "Squall" -> R.drawable.storm
                "clouds", "Fog", "Mist", "Haze", "Smoke" -> R.drawable.cloudy
                "snow" -> R.drawable.snowy
                //"cloudy sunny" -> R.drawable.cloudy_sunny
                else -> R.drawable.sunny
            }
        }
    }
}