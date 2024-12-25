package com.example.team_15_ctis_487.retrofitmodel

data class Nutrition(
    val lose_weight: Map<String, List<String>>, // Nutrition for losing weight
    val gain_weight: Map<String, List<String>>, // Nutrition for gaining weight
    val maintain_weight: List<String>             // Nutrition for maintaining weight
) {
    override fun toString(): String {
        return "Nutrition(lose_weight=$lose_weight, gain_weight=$gain_weight, maintain_weight=$maintain_weight)"
    }
}