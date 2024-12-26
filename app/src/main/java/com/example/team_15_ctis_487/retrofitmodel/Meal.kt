package com.example.team_15_ctis_487.retrofitmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.team_15_ctis_487.Utils
import com.google.gson.annotations.SerializedName

@Entity(tableName = Utils.TABLENAME_NUTRITION) // Specify the table name for Room
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @SerializedName("meal")
    var meal: String,   // Name of the meal
    @SerializedName("calories")
    var calories: Int   // Calorie content of the meal
) {
    override fun toString(): String {
        return "Meal(id=$id, meal='$meal', calories=$calories)"
    }
}