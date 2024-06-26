package com.example.weatherapplication.presentation.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherapplication.core.base.BaseFragment
import com.example.weatherapplication.core.functional.Resource
import com.example.weatherapplication.presentation.presentation.adapter.WeatherAdapter
import com.example.weatherapplication.databinding.FragmentWeatherListBinding
import com.example.weatherapplication.presentation.decoration.OffsetDecoration
import com.example.weatherapplication.presentation.model.WeatherList
import androidx.navigation.fragment.findNavController


@AndroidEntryPoint
class WeatherListFragment :
    BaseFragment<FragmentWeatherListBinding>(FragmentWeatherListBinding::inflate) {
    private lateinit var adapter: WeatherAdapter
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val args = WeatherListFragmentArgs.fromBundle(requireArguments())

        viewModel.getAllData(args.city)

        setupRecyclerView()

        observeWeatherListLiveData()

        adapterItemClick()

    }

    private fun adapterItemClick() {
        adapter.itemClick = { weather ->
            findNavController().navigate(
                WeatherListFragmentDirections.actionWeatherListFragmentToDetailedFragment(
                    weather.name!!,
                    weather.temp!!.toString(),
                    weather.condition?.text!!,
                    weather.wind!!.toString(),
                )
            )
        }
    }

    private fun observeWeatherListLiveData() {
        viewModel.weatherListLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loading.isVisible = true
                }

                is Resource.Success -> {
                    binding.loading.isVisible = false

                    val weatherData = resource.data
                    weatherData?.let {
                        if (!weatherList.contains(weatherData)) {
                            weatherList.add(weatherData)
                        }
                    }
                    adapter.submitList(weatherList)
                }

                is Resource.Error -> {
                    binding.loading.isVisible = false

                    Toast.makeText(this.context, resource.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> Unit
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = WeatherAdapter()

        binding.weatherRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WeatherListFragment.adapter
            setHasFixedSize(true)

            val offsetDecoration = OffsetDecoration(start = 4, top = 20, end = 2, bottom = 16)
            binding.weatherRecyclerView.addItemDecoration(offsetDecoration)
        }
    }

    companion object {
        val weatherList = mutableListOf<WeatherList>()
    }

}

