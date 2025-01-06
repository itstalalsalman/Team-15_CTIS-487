package com.example.team_15_ctis_487

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.team_15_ctis_487.databinding.ActivityHomeFragmentBinding // Ensure this import exists

class HomeFragment : Fragment() {
    private var _binding: ActivityHomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityHomeFragmentBinding.inflate(inflater, container, false)

        binding.btnCheckBMI.setOnClickListener {
            val intent = Intent(requireContext(), BMICheckingActivity::class.java)
            startActivity(intent)
        }

        binding.btnSeeLift.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_track)
        }

        binding.btnSeeNutrition.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_nutrition)
        }

        binding.btnGoToGoals.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_goal)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
