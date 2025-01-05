package com.example.team_15_ctis_487

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.workerjsondatabase.db.ExerciseDAO
import com.ctis487.workerjsondatabase.db.FitnessRoomDatabase
import com.example.team_15_ctis_487.adapter.ExerciseAdapter
import com.example.team_15_ctis_487.databinding.ActivityTrackWorkoutFragmentBinding
import com.example.team_15_ctis_487.retrofitmodel.Exercise
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackWorkoutFragment : Fragment() {

    private var _binding: ActivityTrackWorkoutFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseDAO: ExerciseDAO
    private lateinit var exerciseAdapter: ExerciseAdapter
    private var exerciseList: List<Exercise> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityTrackWorkoutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called when the view is fully created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain DAO
        exerciseDAO = FitnessRoomDatabase.getDatabase(requireContext()).exerciseDao()

        // Set up RecyclerView
        setupRecyclerView()

        // Load exercises from DB
        loadExercises()

        // Set up FloatingActionButton for inserting a new Exercise
        binding.fabAddExercise.setOnClickListener {
            showInsertDialog()
        }

        binding.buttonBeginner.setOnClickListener {
            filterExercises("Beginner")
        }
        binding.buttonIntermediate.setOnClickListener {
            filterExercises("Intermediate")
        }
        binding.buttonAdvanced.setOnClickListener {
            filterExercises("Advanced")
        }
    }

    private fun setupRecyclerView() {
        exerciseAdapter = ExerciseAdapter(emptyList()) { exercise ->
            // Update callback
            showUpdateDialog(exercise)
        }
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewExercises.adapter = exerciseAdapter

        // Swipe to delete
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val exerciseToDelete = exerciseAdapter.getExerciseAtPosition(position)
                deleteExercise(exerciseToDelete)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerViewExercises)
    }

    private fun loadExercises() {
        lifecycleScope.launch {
            exerciseList = withContext(Dispatchers.IO) {
                exerciseDAO.getAllExcercises() // from your DAO
            }
            exerciseAdapter.updateExercises(exerciseList)
        }
    }

    private fun showInsertDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_exercise, null)
        val exerciseNameEdit =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.editExerciseName
            )
        val exerciseSetsEdit =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.editExerciseSets
            )
        val exerciseRepsEdit =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.editExerciseReps
            )

        // Spinner for exercise level
        val spinnerLevel = dialogView.findViewById<Spinner>(R.id.spinnerLevel)
        setupSpinner(spinnerLevel, "Beginner") // default selection is "Beginner"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Exercise")
            .setView(dialogView)
            .setPositiveButton("Insert") { dialog, _ ->
                val name = exerciseNameEdit.text.toString()
                val sets = exerciseSetsEdit.text.toString().toIntOrNull() ?: 0
                val reps = exerciseRepsEdit.text.toString().toIntOrNull() ?: 0
                val level = spinnerLevel.selectedItem.toString()

                if (name.isEmpty()) {
                    Toast.makeText(context, "Please enter a name.", Toast.LENGTH_SHORT).show()
                } else {
                    // Create a new Exercise object
                    val newExercise = Exercise(
                        exercise = name,
                        sets = sets,
                        reps = reps,
                        level = level
                    )
                    insertExercise(newExercise)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun insertExercise(exercise: Exercise) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                exerciseDAO.insertExercise(exercise)
            }
            loadExercises()
        }
    }

    private fun showUpdateDialog(oldExercise: Exercise) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_exercise, null)
        val exerciseNameEdit =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.editExerciseName
            )
        val exerciseSetsEdit =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.editExerciseSets
            )
        val exerciseRepsEdit =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
                R.id.editExerciseReps
            )

        // Set existing data
        exerciseNameEdit.setText(oldExercise.exercise)
        exerciseSetsEdit.setText(oldExercise.sets.toString())
        exerciseRepsEdit.setText(oldExercise.reps.toString())

        // Spinner for exercise level
        val spinnerLevel = dialogView.findViewById<Spinner>(R.id.spinnerLevel)
        setupSpinner(spinnerLevel, oldExercise.level)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update Exercise")
            .setView(dialogView)
            .setPositiveButton("Update") { dialog, _ ->
                val updatedName = exerciseNameEdit.text.toString()
                val updatedSets = exerciseSetsEdit.text.toString().toIntOrNull() ?: 0
                val updatedReps = exerciseRepsEdit.text.toString().toIntOrNull() ?: 0
                val updatedLevel = spinnerLevel.selectedItem.toString()

                // Create a new immutable Exercise object with updated fields
                val updatedExercise = oldExercise.copy(
                    exercise = updatedName,
                    sets = updatedSets,
                    reps = updatedReps,
                    level = updatedLevel
                )

                updateExercise(updatedExercise)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateExercise(exercise: Exercise) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                exerciseDAO.updateExercise(exercise)
            }
            loadExercises()
        }
    }

    private fun deleteExercise(exercise: Exercise) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                exerciseDAO.deleteExercise(exercise)
            }
            loadExercises()
        }
    }

    private fun filterExercises(level: String) {
        // Filter in memory from the full exerciseList
        val filtered = exerciseList.filter { it.level.equals(level, ignoreCase = true) }
        exerciseAdapter.updateExercises(filtered)
    }

    /**
     * Set up the spinner with the levels, defaulting to [defaultLevel].
     */
    private fun setupSpinner(spinner: Spinner, defaultLevel: String) {
        val levels = listOf("Beginner", "Intermediate", "Advanced")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, levels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Pre-select the defaultLevel if found
        val index = levels.indexOf(defaultLevel)
        if (index >= 0) {
            spinner.setSelection(index)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
