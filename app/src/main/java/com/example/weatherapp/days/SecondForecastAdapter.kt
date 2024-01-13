package com.example.weatherapp.days


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.helpers.MyDateTimeFormatter.Companion.formatDateTimeToDay
import com.example.weatherapp.helpers.WhetherImgHelper
import com.example.weatherapp.dataClass.ForecastEntry
import com.example.weatherapp.databinding.ItemDaysBinding

class SecondForecastAdapter :
    RecyclerView.Adapter<SecondForecastAdapter.SecondForecastViewHolder>() {
    private val forecasts = mutableListOf<ForecastEntry>()

    fun setData(data: List<ForecastEntry>) {
        forecasts.clear()
        forecasts.addAll(data)
        notifyDataSetChanged()
    }

    class SecondForecastViewHolder(private val binding: ItemDaysBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textDay = binding.tvDay
        val textMainDesc = binding.tvMainDay
        val textMin = binding.tvMinDegreeDay
        val textMax = binding.tvMaxDegreeDay
        val img = binding.ivForecastDay
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondForecastViewHolder {
        val binding =
            ItemDaysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SecondForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SecondForecastViewHolder, position: Int) {
        val currentForecast = forecasts[position]

        holder.textDay.text = formatDateTimeToDay(currentForecast.dt_txt)
        holder.textMainDesc.text = "${currentForecast.weather[0].main}"
        holder.textMin.text = "${currentForecast.main.temp_min.toInt()}°"
        holder.textMax.text = "${currentForecast.main.temp_max.toInt()}°"


        val weatherDescription = currentForecast.weather[0].main
        holder.img.setImageResource(WhetherImgHelper.setWeatherImage(weatherDescription))

    }

    override fun getItemCount() = forecasts.size
}
