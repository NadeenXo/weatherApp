package com.example.weatherapp.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.helpers.MyDateTimeFormatter.Companion.formatDateTimeToHours
import com.example.weatherapp.helpers.WhetherImgHelper
import com.example.weatherapp.dataClass.ForecastEntry
import com.example.weatherapp.databinding.ItemForecastBinding

class ForecastAdapter :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    private val forecasts = mutableListOf<ForecastEntry>()

    fun setData(data: List<ForecastEntry>) {
        forecasts.clear()
        forecasts.addAll(data)
        notifyDataSetChanged()
    }

    class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textDateTime = binding.tvTime
        val textTemperature = binding.tvDegreeForecast
        val img = binding.ivForecast
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding =
            ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentForecast = forecasts[position]

        holder.textDateTime.text = formatDateTimeToHours(currentForecast.dt_txt)
        holder.textTemperature.text = "${currentForecast.main.temp.toInt()}°"

        val weatherDescription = currentForecast.weather[0].main
        holder.img.setImageResource(WhetherImgHelper.setWeatherImage(weatherDescription))

    }

    override fun getItemCount() = forecasts.size
}
