package com.example.forecastapp.Repository

import com.example.forecastapp.Server.ApiServices

class WeatherRepository(val api:ApiServices) {
    fun getCurrentWeather(lat: Double, lon: Double, unit:String) {
        api.getCurrentWeather(lat,lon,unit,"36611ba4040e86999c3fa75e5bc83aea")
    }
}