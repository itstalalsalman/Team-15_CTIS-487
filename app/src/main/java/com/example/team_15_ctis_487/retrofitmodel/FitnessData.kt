package com.example.team_15_ctis_487.retrofitmodel

data class FitnessData(
    val workouts: Workouts, // Workouts data
    val nutrition: Nutrition // Nutrition data
) {
    override fun toString(): String {
        return "FitnessData(workouts=$workouts, nutrition=$nutrition)"
    }
}