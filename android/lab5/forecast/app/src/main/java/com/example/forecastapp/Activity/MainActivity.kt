package com.example.forecastapp.Activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.forecastapp.Model.CurrentResponseAPI
import com.example.forecastapp.R
import com.example.forecastapp.ViewModel.WeatherViewModel
import com.example.forecastapp.databinding.ActivityMainBinding
import org.chromium.base.Callback
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        binding.apply {
            var lat = 51.50
            var lon = -0.12
            var name = "London"
//                cityTxt.text = name
//                progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric")
                .enqueue(object : Callback<CurrentResponseAPI>,
                    retrofit2.Callback<CurrentResponseAPI> {
                    override fun onResult(result: CurrentResponseAPI?) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(
                        call: Call<CurrentResponseAPI>,
                        response: Response<CurrentResponseAPI>
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(call: Call<CurrentResponseAPI>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }
}
