package com.example.team_15_ctis_487.retrofitmodel


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.team_15_ctis_487.Utils
import com.google.gson.annotations.SerializedName

@Entity(tableName = Utils.TABLENAME_WORKOUT) // Specify the table name for Room
class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @SerializedName("exercise")
    val exercise: String, // Name of the exercise
    @SerializedName("sets")
    val sets: Int,        // Number of sets
    @SerializedName("reps")
    val reps: Int         // Number of repetitions
) {
    override fun toString(): String {
        return "Exercise(id=$id, exercise='$exercise', sets=$sets, reps=$reps)"
    }
}