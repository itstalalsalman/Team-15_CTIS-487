package com.example.team_15_ctis_487.retrofitmodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Exercise(
    val exercise: String, // Name of the exercise
    val sets: Int,        // Number of sets
    val reps: Int         // Number of repetitions
):Parcelable {
    override fun toString(): String {
        return "Exercise(exercise='$exercise', sets=$sets, reps=$reps)"
    }
}