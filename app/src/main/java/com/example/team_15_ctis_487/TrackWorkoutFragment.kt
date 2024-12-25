package com.example.team_15_ctis_487

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.team_15_ctis_487.databinding.ActivityTrackWorkoutFragmentBinding // Ensure this import exists
import com.example.team_15_ctis_487.retrofitmodel.Exercise

class TrackWorkoutFragment : Fragment() {
    private var _binding: ActivityTrackWorkoutFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityTrackWorkoutFragmentBinding.inflate(inflater, container, false)
        val workouts: ArrayList<Exercise>? = arguments?.getParcelableArrayList("workout")
        if (workouts != null) {
            Log.d("TRACK_WORKOUT_FRAGMENT", "Received workouts: $workouts")
        } else {
            Log.d("TRACK_WORKOUT_FRAGMENT", "No workouts data received")
        }
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
