package com.example.weatherapp.days

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.helpers.WhetherImgHelper
import com.example.weatherapp.dataClass.ForecastEntry
import com.example.weatherapp.databinding.ActivitySecondBinding
import com.example.weatherapp.home.MainActivity


class SecondActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySecondBinding
    private lateinit var secondForecastAdapter: SecondForecastAdapter

    private inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? =
        when {
            SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        binding.backToolbar.ivBack.setOnClickListener {
            finish()
        }

        val forecasts = intent.parcelableArrayList<ForecastEntry>("forecasts") ?: emptyList()
        secondForecastAdapter.setData(forecasts)


        sharedPreferences =
            getSharedPreferences(MainActivity.WEATHER_PREFERENCES, Context.MODE_PRIVATE)

        val defaultValueInt = 0
        val defaultValueString = ""

        val temperature = sharedPreferences.getInt("temperature", defaultValueInt)
        val highTemperature = sharedPreferences.getInt("highTemperature", defaultValueInt)
        val lowTemperature = sharedPreferences.getInt("lowTemperature", defaultValueInt)
        val feelsLike = sharedPreferences.getInt("feelsLike", defaultValueInt)
        val sunset = sharedPreferences.getString("sunset", defaultValueString) ?: defaultValueString
        val sunrise =
            sharedPreferences.getString("sunrise", defaultValueString) ?: defaultValueString
        val cityName =
            sharedPreferences.getString("cityName", defaultValueString) ?: defaultValueString
        val mainDescription =
            sharedPreferences.getString("mainDescription", defaultValueString) ?: defaultValueString

        binding.tvCountry.text = cityName
        binding.ivForecastSecond.setImageResource(
            WhetherImgHelper.setWeatherImage(mainDescription)
        )
        binding.tvDegreeSecound.text = "$temperature${getString(R.string.metric)}"
        binding.tvFeelsLike.text = "feels like: $feelsLike${getString(R.string.metric)}"
        binding.tvTempMax.text = "max: $highTemperature${getString(R.string.metric)}"
        binding.tvTempMin.text = "min: $lowTemperature${getString(R.string.metric)}"
        binding.tvSunriseValue.text = sunrise
        binding.tvSunsetValue.text = sunset
    }

    private fun setRecyclerView() {
        val rv = binding.recyclerView
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        secondForecastAdapter = SecondForecastAdapter()
        rv.adapter = secondForecastAdapter
    }

}