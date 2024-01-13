package com.example.weatherapp.home

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.Network.Retrofit
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.helpers.MyDateTimeFormatter.Companion.formatDateTime
import com.example.weatherapp.helpers.MyDateTimeFormatter.Companion.formatDateTimeToTime
import com.example.weatherapp.R
import com.example.weatherapp.days.SecondActivity
import com.example.weatherapp.helpers.WhetherImgHelper
import com.example.weatherapp.dataClass.CurrentWeatherResponse
import com.example.weatherapp.dataClass.WeatherResponse
import java.time.format.DateTimeFormatter
import java.time.LocalDate

//todo:
//loading
//splashscreen :: done
//corotiens
//mvvm


class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        const val WEATHER_PREFERENCES = "WeatherPreferences"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var forecastAdapter: ForecastAdapter
    private var isLocationUpdated = false
    private lateinit var locationManager: LocationManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCardCases()
        setRecyclerView()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getUserLocation()

        sharedPreferences = getSharedPreferences(WEATHER_PREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

    }

    private fun setCardCases() {
        binding.cardHumidityCase.ivMainCase.setImageResource(R.drawable.humidity)
        binding.cardHumidityCase.tvMainCase.text = getString(R.string.humidity_msg)
        binding.cardWindCase.tvMainCase.text = getString(R.string.wind_speed_msg)
        binding.cardWindCase.ivMainCase.setImageResource(R.drawable.wind)
    }

    private fun setRecyclerView() {
        val rv = binding.rvForecasts
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        forecastAdapter = ForecastAdapter()
        rv.adapter = forecastAdapter
    }


    private fun getUserLocation() {
        // Check for location permission
        if (checkGrantedPermissions()) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                try {
                    // Request location updates
                    if (!isLocationUpdated) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0f,
                            locationListener
                        )
                        isLocationUpdated = true
                    }
                } catch (e: SecurityException) {
                    // Handle SecurityException (e.g., show a message to the user)
                    Log.e("MainActivity", "SecurityException: ${e.message}")
                }
            } else {
                showEnableLocationDialog()

            }
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkGrantedPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun showEnableLocationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("GPS is disabled. Do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
                // Optionally handle the case where the user chooses not to enable GPS
            }

        val alert = dialogBuilder.create()
        alert.show()
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Handle location updates here
            val userLat = location.latitude
            val userLon = location.longitude

            makeCurrWeatherAPICall(userLat, userLon)
            makeWeatherAPICall(userLat, userLon)

            // Optionally, you may stop receiving updates after obtaining the first location
            locationManager.removeUpdates(this)
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // Handle status changes if needed
        }

        override fun onProviderEnabled(provider: String) {
            // Called when the location provider is enabled
        }

        override fun onProviderDisabled(provider: String) {
            // Called when the location provider is disabled
        }
    }

    private fun makeWeatherAPICall(lat: Double, lon: Double) {
        val api = Retrofit.getInstance()
        val weatherForecastCall = api.getWeatherForecast(lat, lon)

        weatherForecastCall.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    // Process the weather data
                    if (weatherResponse != null) {
                        val forecastList = weatherResponse.list
                        // Group forecast entries by day
                        val groupedForecasts = forecastList.groupBy {
                            LocalDate.parse(
                                it.dt_txt,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            ).dayOfWeek
                        }
                        // Filter forecast entries for the next 5 days
                        val currentDate = LocalDate.now()
                        // Assuming you want forecasts for the next 5 days
                        val nextFiveDays = (0 until 7).map { currentDate.plusDays(it.toLong()) }
                        // Separate the grouped forecasts for each day
                        val dailyForecasts = nextFiveDays.map { day ->
                            groupedForecasts[day.dayOfWeek] ?: emptyList()
                        }
                        // Set data to the first adapter (for the current day)
                        val currentDayForecasts = dailyForecasts.getOrNull(0) ?: emptyList()
                        forecastAdapter.setData(currentDayForecasts)

                        // Get only the first forecast for each day
                        val FiveDaysForecasts = nextFiveDays.flatMap { day ->
                            groupedForecasts[day.dayOfWeek]?.firstOrNull()?.let { listOf(it) }
                                ?: emptyList()
                        }
                        // Set data to the second adapter (for the next 5 days)
                        val nextFiveDaysForecasts = FiveDaysForecasts.drop(1)
                        binding.tvNext.setOnClickListener {
                            val intent = Intent(this@MainActivity, SecondActivity::class.java)
                            intent.putParcelableArrayListExtra(
                                "forecasts",
                                ArrayList(nextFiveDaysForecasts)
                            )
                            startActivity(intent)
                        }
                    }

                } else {
                    // Handle error
                    Log.d(
                        "MainActivity",
                        "Error WeatherResponse: ${response.code()}, ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                // Handle failure
                Log.d("MainActivity", "onFailure WeatherResponse: ${t.message}")
            }
        })
    }

    private fun makeCurrWeatherAPICall(lat: Double, lon: Double) {
        val api = Retrofit.getInstance()
        val weatherForecastCall = api.getCurrentWeather(lat, lon)

        weatherForecastCall.enqueue(object : Callback<CurrentWeatherResponse?> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse?>,
                response: Response<CurrentWeatherResponse?>
            ) {
                if (response.isSuccessful) {
                    val currentWeatherResponse: CurrentWeatherResponse? = response.body()

                    if (currentWeatherResponse != null) {
                        val description = currentWeatherResponse.weather[0].description
                        val mainDescription = currentWeatherResponse.weather[0].main
                        val main = currentWeatherResponse.main
                        val temp = main.temp.toInt()
                        val time = formatDateTime(currentWeatherResponse.dt.toString())
                        val rainPercentage = currentWeatherResponse.rain?.`1h`?.toInt()
                        val windSpeed = currentWeatherResponse.wind?.speed?.toInt()
                        val humidity = main.humidity
                        val highTemperature = main.temp_max.toInt()
                        val lowTemperature = main.temp_min.toInt()
                        val feelsLike = main.feels_like.toInt()

                        val cityName = currentWeatherResponse.sys.country
                        val sunrise =
                            formatDateTimeToTime(currentWeatherResponse.sys.sunrise.toString())
                        val sunset =
                            formatDateTimeToTime(currentWeatherResponse.sys.sunset.toString())

                        // Now you can use these values as needed
                        mapValuesToViews(
                            description,
                            time,
                            temp,
                            highTemperature,
                            lowTemperature,
                            rainPercentage,
                            windSpeed,
                            humidity,
                            mainDescription
                        )

                        saveToPreferences(
                            temp, rainPercentage, windSpeed,
                            humidity, highTemperature, lowTemperature,
                            feelsLike, cityName, sunrise, sunset, mainDescription
                        )
                    } else {
                        Log.d(
                            "MainActivity",
                            "Error CurrentWeatherResponse: ${response.code()}, ${
                                response.errorBody()?.string()
                            }"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse?>, t: Throwable) {
                Log.d("MainActivity", "onFailure CurrentWeatherResponse: ${t.message}")
            }
        })
    }

    private fun mapValuesToViews(
        description: String,
        time: String,
        temp: Int,
        highTemperature: Int,
        lowTemperature: Int,
        rainPercentage: Int?,
        windSpeed: Int?,
        humidity: Int,
        mainDescription: String
    ) {
        binding.tvDescription.text = description
        binding.tvDatetime.text = time
        binding.tvDegree.text = "$temp${getString(R.string.metric)}"
        binding.tvHighLow.text = getString(
            R.string.h_l,
            highTemperature.toString(),
            getString(R.string.metric),
            lowTemperature.toString(),
            getString(R.string.metric)
        )
        binding.cardRainCase.tvPersentageCase.text =
            if (rainPercentage != null) "${rainPercentage}%" else "0%"
        binding.cardWindCase.tvPersentageCase.text =
            if (windSpeed != null) "$windSpeed m/s" else "0 m/s"
        binding.cardHumidityCase.tvPersentageCase.text = "${humidity}%"
        binding.ivWeather.setImageResource(
            WhetherImgHelper.setWeatherImage(
                mainDescription
            )
        )
    }

    private fun saveToPreferences(
        temp: Int,
        rainPercentage: Int?, windSpeed: Int?, humidity: Int,
        highTemperature: Int, lowTemperature: Int, feelsLike: Int,
        cityName: String, sunrise: String, sunset: String, main: String
    ) {
        editor.putInt("temperature", temp)
        editor.putInt("rainPercentage", rainPercentage ?: 0)
        editor.putInt("windSpeed", windSpeed ?: 0)
        editor.putInt("humidity", humidity)
        editor.putInt("highTemperature", highTemperature)
        editor.putInt("lowTemperature", lowTemperature)
        editor.putInt("feelsLike", feelsLike)
        editor.putString("sunset", sunset)
        editor.putString("sunrise", sunrise)
        editor.putString("cityName", cityName)
        editor.putString("mainDescription", main)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        isLocationUpdated = false
    }
}