package com.example.team_15_ctis_487.retrofitmodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Workouts(
    val lose_weight: Map<String, List<Exercise>>, // Workouts for losing weight
    val gain_weight: Map<String, List<Exercise>>, // Workouts for gaining weight
    val maintain_weight: List<Exercise>            // Workouts for maintaining weight
):Parcelable {
    override fun toString(): String {
        return "Workouts(lose_weight=$lose_weight, gain_weight=$gain_weight, maintain_weight=$maintain_weight)"
    }
}