package com.example.weatherapplication.data.api

import com.example.weatherapplication.BuildConfig
import com.example.weatherapplication.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY,
        @Query("q") city: String,
        @Query("lang") language: String,
    ): WeatherResponse
    @GET("current.json")
    suspend fun getAllData(
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY,
        @Query("q") city: String,
        @Query("lang") language: String,
    ): WeatherResponse

}