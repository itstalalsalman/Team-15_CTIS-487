package com.ctis487.workerjsondatabase.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.team_15_ctis_487.Utils
import com.example.team_15_ctis_487.retrofitmodel.Exercise
import com.example.team_15_ctis_487.retrofitmodel.Meal

@Dao
interface ExerciseDAO {
    // Insert a single exercise
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    // Insert a list of exercises
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    // Update an existing exercise
    @Update
    suspend fun updateExercise(exercise: Exercise)

    // Delete a specific exercise
    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    // Get all exercises from the database
    @Query("SELECT * FROM ${Utils.TABLENAME_WORKOUT} ORDER BY exercise")
    suspend fun getAllExcercises(): List<Exercise>

    // Delete all exercises from the database
    @Query("DELETE FROM ${Utils.TABLENAME_WORKOUT}")
    suspend fun deleteAllExercises()
}