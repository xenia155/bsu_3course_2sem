package com.example.weatherapplication.data.repository

import com.example.weatherapplication.data.model.WeatherResponse
import com.example.weatherapplication.presentation.model.WeatherInfo
import com.example.weatherapplication.presentation.model.WeatherList

fun WeatherResponse.toPresentation(): WeatherInfo = WeatherInfo(
    name = location?.name,
    region = location?.region,
    country = location?.country,
    lat = location?.lat,
    lon = location?.lon,
    timeZone = location?.timeZone,
    localtime = location?.localtime,
    lastUpdate = current?.lastUpdate,
    temp = current?.temp,
    isDay = current?.isDay,
    wind = current?.wind,
    windDegree = current?.windDegree,
    windDirection = current?.windDirection,
    condition = current?.condition,
)

fun WeatherResponse.toAdapter(): WeatherList = WeatherList(
    name = location?.name,
    region = location?.region,
    country = location?.country,
    temp = current?.temp,
    wind = current?.wind,
    condition = current?.condition,
)


