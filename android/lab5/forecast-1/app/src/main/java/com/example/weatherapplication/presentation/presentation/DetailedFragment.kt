package com.example.weatherapplication.presentation.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherapplication.core.base.BaseFragment
import com.example.weatherapplication.databinding.FragmentDetailedBinding

@AndroidEntryPoint
class DetailedFragment : BaseFragment<FragmentDetailedBinding>(FragmentDetailedBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDataFromItemClick()
    }

    @SuppressLint("SetTextI18n")
    private fun setDataFromItemClick() {
        val args = DetailedFragmentArgs.fromBundle(requireArguments())
        binding.name.text = args.city
        binding.temperature.text = "${args.temperature}°"
        binding.wind.text = "${args.windSpeed} km/h"
        binding.weatherCondition.text = args.weatherCondition
    }
}