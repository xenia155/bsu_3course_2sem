package com.example.forecastapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.forecastapp.Repository.WeatherRepository
import com.example.forecastapp.Server.ApiClient
import com.example.forecastapp.Server.ApiServices

class WeatherViewModel(val repository: WeatherRepository):ViewModel() {
    constructor():this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat: Double, lon: Double, unit: String) =
        repository.getCurrentWeather(lat,lon,unit)
}