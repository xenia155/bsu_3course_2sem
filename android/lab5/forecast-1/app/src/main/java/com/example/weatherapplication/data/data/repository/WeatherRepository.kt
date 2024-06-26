package com.example.weatherapplication.data.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.weatherapplication.core.base.BaseRepository
import com.example.weatherapplication.core.functional.State
import com.example.weatherapplication.data.api.WeatherApi
import com.example.weatherapplication.data.repository.toAdapter
import com.example.weatherapplication.data.repository.toPresentation
import com.example.weatherapplication.presentation.model.WeatherInfo
import com.example.weatherapplication.presentation.model.WeatherList
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) : BaseRepository() {


    suspend fun getCurrentWeather(city: String): State<Throwable, WeatherInfo> = apiCall {
        withContext(Dispatchers.IO) {
            api.getCurrentWeather(city = city, language = "en").toPresentation()
        }
    }

    suspend fun getAllData(city: String): State<Throwable, WeatherList> = apiCall {
        withContext(Dispatchers.IO) {
            api.getAllData(city = city, language = "en").toAdapter()
        }
    }
}
