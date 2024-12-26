package com.ctis487.workerjsondatabase.backgroundservice

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ctis487.workerjsondatabase.db.FitnessRoomDatabase
import com.example.team_15_ctis_487.Utils
import com.example.team_15_ctis_487.retrofitmodel.Exercise
import com.example.team_15_ctis_487.retrofitmodel.Meal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Retrieve input data
        Log.d(Utils.TAGFORLAGCAT, "Meals JSON:")
        val mealsJson = inputData.getString("nutrition") ?: return Result.failure()
        val exercisesJson = inputData.getString("workouts") ?: return Result.failure()
        Log.d(Utils.TAGFORLAGCAT, "Code did not reach over here")

        // Log the input data
        Log.d(Utils.TAGFORLAGCAT, "Meals JSON: $mealsJson")
        Log.d(Utils.TAGFORLAGCAT, "Exercises JSON: $exercisesJson")

        // Convert JSON strings to lists
        val meals: List<Meal> = Gson().fromJson(mealsJson, object : TypeToken<List<Meal>>() {}.type)
        val exercises: List<Exercise> = Gson().fromJson(exercisesJson, object : TypeToken<List<Exercise>>() {}.type)

        // Get DAO instances
        val mealDAO = FitnessRoomDatabase.getDatabase(applicationContext).mealDao()
        val exerciseDAO = FitnessRoomDatabase.getDatabase(applicationContext).exerciseDao()

        return try {
            Log.d(Utils.TAGFORLAGCAT, "Inserting meals and exercises into the database")

            // Insert meals into the database
            mealDAO.insertMeals(meals)

            // Insert exercises into the database
            exerciseDAO.insertExercises(exercises)

            // Create the output of the worker
            val outputData = Data.Builder().putString("result", "Meals and exercises inserted").build()
            Log.d(Utils.TAGFORLAGCAT, "End of worker")
            Result.success(outputData) // Return success
        } catch (throwable: Throwable) {
            Log.e(Utils.TAGFORLAGCAT, "Error inserting data: ${throwable.message}", throwable)
            Result.failure() // Return failure
        }
    }
}