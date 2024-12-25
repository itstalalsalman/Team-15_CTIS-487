package com.example.team_15_ctis_487

import NutritionViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.team_15_ctis_487.databinding.ActivityNutritionFragmentBinding // Ensure this import exists

class NutritionFragment : Fragment() {
    private var _binding: ActivityNutritionFragmentBinding? = null
    private val binding get() = _binding!!
    private val nutritionViewModel: NutritionViewModel by activityViewModels() // Get the shared ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityNutritionFragmentBinding.inflate(inflater, container, false)
        nutritionViewModel.nutritionData.observe(viewLifecycleOwner) { nutrition ->
            // Log the nutrition data
            Log.d("NUTRITION_FRAGMENT", "Received nutrition data: $nutrition")


        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
