package com.example.team_15_ctis_487

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team_15_ctis_487.retrofitmodel.Meal
import com.google.android.material.button.MaterialButton

class MealAdapter(
    private var meals: List<Meal>,
    private val onUpdateClick: (Meal) -> Unit // Callback for update action
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealName: TextView = itemView.findViewById(R.id.mealName)
        val mealCalories: TextView = itemView.findViewById(R.id.mealCalories)
        val buttonUpdate: MaterialButton = itemView.findViewById(R.id.buttonUpdate) // Reference to the update button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.mealName.text = meal.meal
        holder.mealCalories.text = "${meal.calories} calories"

        // Set up the update button click listener
        holder.buttonUpdate.setOnClickListener {
            onUpdateClick(meal) // Trigger the callback with the meal to update
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun updateMeals(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged() // Notify the adapter to refresh the data
    }

    // Method to get a meal at a specific position
    fun getMealAtPosition(position: Int): Meal {
        return meals[position]
    }
}