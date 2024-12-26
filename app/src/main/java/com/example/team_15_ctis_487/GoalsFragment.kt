package com.example.team_15_ctis_487

import android.os.Bundle
import androidx.work.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.team_15_ctis_487.databinding.ActivityGoalsFragmentBinding
import com.example.team_15_ctis_487.json.ApiClient
import com.example.team_15_ctis_487.retrofitmodel.FitnessData
import com.example.team_15_ctis_487.json.ApiService
import com.example.team_15_ctis_487.retrofitmodel.Exercise
import retrofit2.Call
import retrofit2.Callback
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ctis487.workerjsondatabase.backgroundservice.CustomWorker
import com.ctis487.workerjsondatabase.db.FitnessRoomDatabase
import com.example.team_15_ctis_487.retrofitmodel.Meal
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GoalsFragment : Fragment() {
    private var _binding: ActivityGoalsFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var workManager: WorkManager
    lateinit var workRequest: OneTimeWorkRequest
    private lateinit var apiService: ApiService
    private var selectedButton: RadioButton? = null
    private var fitnessData: FitnessData? = null // Make it nullable
    private var isDataLoaded = false // Flag to check if data is loaded
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityGoalsFragmentBinding.inflate(inflater, container, false)

        apiService = ApiClient.getClient().create(ApiService::class.java)
        val request = apiService.getParentJSONObject()

        Log.d("CURRENCY", "Before Request")
        request.enqueue(object : Callback<FitnessData> {
            override fun onFailure(call: Call<FitnessData>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_LONG).show()
                Log.d("CURRENCY", "Error: " + t.message.toString())
            }

            override fun onResponse(call: Call<FitnessData>, response: Response<FitnessData>) {
                Log.d("CURRENCY", "Response taken\n" + response.toString() + "\n" + response.body())
                if (response.isSuccessful) {
                    fitnessData = response.body() // Store the fetched data
                    Log.d("CURRENCY", "Response object taken from server\n" + fitnessData.toString())
                    isDataLoaded = true // Set the flag to true
                    enableWeightCategoryRadioButtons() // Enable radio buttons
                }
            }
        })

        // Set click listeners for individual RadioButtons
        binding.radioLoseWeight.setOnClickListener {
            handleRadioButtonClick(binding.radioLoseWeight)
        }

        binding.radioGainWeight.setOnClickListener {
            handleRadioButtonClick(binding.radioGainWeight)
        }

        binding.radioMaintainWeight.setOnClickListener {
            handleRadioButtonClick(binding.radioMaintainWeight)
        }

        binding.radio05kg.setOnClickListener {
            // Deselect other buttons
            binding.radio1kg.isChecked = false
            binding.radio2kg.isChecked = false
            filterData() // Call filterData after selection
        }

        binding.radio1kg.setOnClickListener {
            // Deselect other buttons
            binding.radio05kg.isChecked = false
            binding.radio2kg.isChecked = false
            filterData() // Call filterData after selection
        }

        binding.radio2kg.setOnClickListener {
            // Deselect other buttons
            binding.radio05kg.isChecked = false
            binding.radio1kg.isChecked = false
            filterData() // Call filterData after selection
        }

        disableWeightCategoryRadioButtons()
        workManager = context?.let { WorkManager.getInstance(it) }!!

        return binding.root
    }

    private fun enableWeightCategoryRadioButtons() {
        binding.radio05kg.isEnabled = true
        binding.radio1kg.isEnabled = true
        binding.radio2kg.isEnabled = true
    }

    private fun disableWeightCategoryRadioButtons() {
        binding.radio05kg.isEnabled = false
        binding.radio1kg.isEnabled = false
        binding.radio2kg.isEnabled = false
    }

    private fun filterData() {
        if (!isDataLoaded) {
            Toast.makeText(requireContext(), "Data not loaded yet. Please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        val data = fitnessData ?: return

        val userGoal = when {
            binding.radioLoseWeight.isChecked -> "lose_weight"
            binding.radioGainWeight.isChecked -> "gain_weight"
            binding.radioMaintainWeight.isChecked -> "maintain_weight"
            else -> null
        }

        val weightCategory = when {
            binding.radio05kg.isChecked -> "0.5kg"
            binding.radio1kg.isChecked -> "1kg"
            binding.radio2kg.isChecked -> "2kg"
            else -> null
        }

        if (userGoal != null) {
            when (userGoal) {
                "lose_weight" -> {
                    weightCategory?.let {
                        val workouts = data.workouts.lose_weight[it]
                        Log.d("FILTERED_WORKOUTS", "Filtered Workouts for $userGoal${weightCategory.let { " ($it)" } ?: ""}: $workouts")
                        val nutrition = data.nutrition.lose_weight[it]
                        Log.d("FILTERED_NUTRITION", "Filtered Nutrition for $userGoal${weightCategory.let { " ($it)" } ?: ""}: $nutrition")
                        enqueueDataInsertion(workouts,nutrition)


                    }
                }
                "gain_weight" -> {
                    weightCategory?.let {
                        val workouts = data.workouts.gain_weight[it]
                        Log.d("FILTERED_WORKOUTS", "Filtered Workouts for $userGoal${weightCategory.let { " ($it)" } ?: ""}: $workouts")
                        val nutrition = data.nutrition.gain_weight[it]
                        Log.d("FILTERED_NUTRITION", "Filtered Nutrition for $userGoal${weightCategory.let { " ($it)" } ?: ""}: $nutrition")

                        enqueueDataInsertion(workouts,nutrition)


                    }
                }
                "maintain_weight" -> {
                    // No weight category needed for maintain weight
                    val workouts = data.workouts.maintain_weight
                    Log.d("FILTERED_WORKOUTS", "Filtered Workouts for $userGoal: $workouts")
                    val nutrition = data.nutrition.maintain_weight
                    Log.d("FILTERED_NUTRITION", "Filtered Nutrition for $userGoal: $nutrition")
                    enqueueDataInsertion(workouts,nutrition)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please select a goal and weight category.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun enqueueDataInsertion(workouts: List<Exercise>?, nutrition: List<Meal>?) {
        val mealDAO = context?.let { FitnessRoomDatabase.getDatabase(it).mealDao() }
        val exerciseDAO = context?.let { FitnessRoomDatabase.getDatabase(it).exerciseDao() }
        lifecycleScope.launch {
            // Fetch the current meals from the database
            val currentMeals = withContext(Dispatchers.IO) {
                mealDAO?.getAllMeals()
                    ?: emptyList() // Use a safe call and provide a default empty list
            }
            if (!currentMeals.isEmpty()) {
                mealDAO?.deleteAllMeals()
                exerciseDAO?.deleteAllExercises()
            }


            // Convert the workouts and nutrition to JSON strings
            val workoutsJson = Gson().toJson(workouts)
            val nutritionJson = Gson().toJson(nutrition)
            Log.d(Utils.TAGFORLAGCAT, "Meals JSON: $nutritionJson")
            Log.d(Utils.TAGFORLAGCAT, "Exercises JSON: $workoutsJson")
            // Create input data for the worker
            val inputData = Data.Builder()
                .putString("workouts", workoutsJson)
                .putString("nutrition", nutritionJson)
                .build()

            // Create a OneTimeWorkRequest
            workRequest = OneTimeWorkRequest.Builder(CustomWorker::class.java)
                .setInputData(inputData)
                .build()

            // Enqueue the work request
            workManager.enqueue(workRequest)

            // Observe the work request status
            workManager.getWorkInfoByIdLiveData(workRequest.id)
                .observe(viewLifecycleOwner, { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        val resultData: Data = workInfo.outputData // Get output of worker
                        Snackbar.make(
                            binding.root,
                            "Data insertion succeeded: " + resultData.getString("result"),
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else if (workInfo != null && workInfo.state.isFinished) {
                        // Handle failure case
                        Snackbar.make(binding.root, "Data insertion failed", Snackbar.LENGTH_LONG)
                            .show()
                    }
                })
        }


    }

        private fun handleRadioButtonClick(clickedButton: RadioButton) {
        // Deselect the previously selected button if it's not the same as the clicked one
        if (selectedButton != null && selectedButton != clickedButton) {
            selectedButton?.isChecked = false
        }

        // Set the clicked button as the selected button
        selectedButton = clickedButton

        // Show or hide subcategories based on the selected button
        when (clickedButton.id) {
            R.id.radioLoseWeight, R.id.radioGainWeight -> {
                binding.layoutSubcategories.visibility = View.VISIBLE
                // Check if any subcategory is selected
                if (binding.radio05kg.isChecked || binding.radio1kg.isChecked || binding.radio2kg.isChecked) {
                    filterData() // Call filterData if a subcategory is selected
                } else {
                    Toast.makeText(requireContext(), "Please select a weight category", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.radioMaintainWeight -> {
                binding.layoutSubcategories.visibility = View.GONE
                filterData() // Call filterData when maintain weight is selected
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}