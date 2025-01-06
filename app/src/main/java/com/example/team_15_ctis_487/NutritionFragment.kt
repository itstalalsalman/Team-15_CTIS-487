package com.example.team_15_ctis_487

import android.os.Bundle
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctis487.workerjsondatabase.db.FitnessRoomDatabase
import com.ctis487.workerjsondatabase.db.MealDAO
import com.example.team_15_ctis_487.databinding.ActivityNutritionFragmentBinding
import com.example.team_15_ctis_487.retrofitmodel.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NutritionFragment : Fragment() {
    private var _binding: ActivityNutritionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mealDAO: MealDAO
    private lateinit var mealAdapter: MealAdapter
    private var nutrition: List<Meal> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityNutritionFragmentBinding.inflate(inflater, container, false)
        mealDAO = context?.let { FitnessRoomDatabase.getDatabase(it).mealDao() }!!

        // Set up RecyclerView
        setupRecyclerView()

        // Load meals from the database
        loadMeals()

        // Set up button click listeners for filtering
        setupFilterButtons()

        // Set up swipe to delete functionality
        setupSwipeToDelete()

        binding.fabAddMeal.setOnClickListener {
            showAddDialog() // Show the add dialog when the FAB is clicked
        }


        return binding.root
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(emptyList()) { meal ->
            showUpdateDialog(meal) // Pass the meal to the update dialog
        } // Initialize with an empty list and the update click listener
        binding.recyclerViewMeals.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMeals.adapter = mealAdapter
    }

    private fun loadMeals() {
        lifecycleScope.launch {
            nutrition = withContext(Dispatchers.IO) {
                mealDAO.getAllMeals() // Fetch meals from the database
            }
            Log.d(Utils.TAGFORLAGCAT, "$nutrition")
            mealAdapter.updateMeals(nutrition) // Update the RecyclerView with all meals
        }
    }

    private fun setupFilterButtons() {
        binding.buttonLowCalories.setOnClickListener {
            val lowCalMeals = nutrition.filter { it.calories <= 150 }
            updateRecyclerView(lowCalMeals, "Low Calories")
        }

        binding.buttonMediumCalories.setOnClickListener {
            val mediumCalMeals = nutrition.filter { it.calories in 151..300 }
            updateRecyclerView(mediumCalMeals, "Medium Calories")
        }

        binding.buttonHighCalories.setOnClickListener {
            val highCalMeals = nutrition.filter { it.calories > 300 }
            updateRecyclerView(highCalMeals, "High Calories")
        }
    }

    private fun updateRecyclerView(filteredMeals: List<Meal>, category: String) {
        if (filteredMeals.isEmpty()) {
            binding.noMealsMessage.text = "No meals are of $category"
            binding.noMealsMessage.visibility = View.VISIBLE
            binding.recyclerViewMeals.visibility = View.GONE
        } else {
            binding.noMealsMessage.visibility = View.GONE
            binding.recyclerViewMeals.visibility = View.VISIBLE
            mealAdapter.updateMeals(filteredMeals) // Update the RecyclerView with filtered meals
        }
    }
    private fun showUpdateDialog(meal: Meal) {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_meal, null)

        // Create the dialog using MaterialAlertDialogBuilder
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Get references to the EditText fields and button
        val editMealName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editMealName)
        val editMealCalories = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editMealCalories)
        val buttonUpdateMeal = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonUpdateMeal)

        // Set the current meal data in the EditText fields
        editMealName.setText(meal.meal)
        editMealCalories.setText(meal.calories.toString())

        // Handle the update button click
        buttonUpdateMeal.setOnClickListener {
            val updatedMealName = editMealName.text.toString()
            val updatedCalories = editMealCalories.text.toString().toIntOrNull()

            if (updatedCalories != null) {
                // Update the meal object
                meal.meal = updatedMealName
                meal.calories = updatedCalories

                // Call the method to update the meal in the database
                updateMeal(meal)

                // Dismiss the dialog
                dialog.dismiss()
            } else {
                // Handle invalid input (e.g., show a Toast)
                Toast.makeText(context, "Please enter valid calories", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the dialog
        dialog.show()
    }

    private fun showAddDialog() {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_meal, null)

        // Create the dialog using MaterialAlertDialogBuilder
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Get references to the EditText fields and button
        val editMealName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editMealName)
        val editMealCalories = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editMealCalories)
        val buttonAddMeal = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.buttonUpdateMeal)

        // Update the button text to "Add"
        buttonAddMeal.text = "Add"

        // Handle the add button click
        buttonAddMeal.setOnClickListener {
            val mealName = editMealName.text.toString()
            val mealCalories = editMealCalories.text.toString().toIntOrNull()

            if (mealName.isNotBlank() && mealCalories != null) {
                val newMeal = Meal(meal = mealName, calories = mealCalories) // Create a new Meal object

                // Insert the new meal into the database
                insertMeal(newMeal)

                // Dismiss the dialog
                dialog.dismiss()
            } else {
                // Handle invalid input (e.g., show a Toast)
                Toast.makeText(context, "Please provide valid inputs", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the dialog
        dialog.show()
    }

    private fun updateMeal(meal: Meal) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mealDAO.updateMeal(meal) // Update the meal in the database
            }
            loadMeals() // Reload meals to refresh the RecyclerView
        }
    }

    private fun insertMeal(meal: Meal) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mealDAO.insertMeal(meal) // Insert the new meal into the database
            }
            loadMeals() // Reload meals to refresh the RecyclerView
        }
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // We are not moving items
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val mealToDelete = mealAdapter.getMealAtPosition(position) // Get the meal to delete
                deleteMeal(mealToDelete) // Call the delete function
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerViewMeals)
    }

    private fun deleteMeal(meal: Meal) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mealDAO.deleteMealById(meal.id) // Delete from the database
            }
            loadMeals() // Reload meals to refresh the RecyclerView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}