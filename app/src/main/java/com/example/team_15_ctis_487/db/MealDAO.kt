package com.ctis487.workerjsondatabase.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.team_15_ctis_487.Utils
import com.example.team_15_ctis_487.retrofitmodel.Meal

@Dao
interface MealDAO {
    // Insert a list of meals into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<Meal>)

    // Insert a single meal into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    // Get all meals from the database
    @Query("SELECT * FROM ${Utils.TABLENAME_NUTRITION}")
    suspend fun getAllMeals(): List<Meal>

    // Update an existing meal
    @Update
    suspend fun updateMeal(meal: Meal)

    // Delete a single meal by its ID
    @Query("DELETE FROM ${Utils.TABLENAME_NUTRITION} WHERE id = :mealId")
    suspend fun deleteMealById(mealId: Int)

    // Delete all meals from the database
    @Query("DELETE FROM ${Utils.TABLENAME_NUTRITION}")
    suspend fun deleteAllMeals()
}