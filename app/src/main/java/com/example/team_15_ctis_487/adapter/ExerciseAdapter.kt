package com.example.team_15_ctis_487.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team_15_ctis_487.R
import com.example.team_15_ctis_487.retrofitmodel.Exercise
import com.google.android.material.button.MaterialButton

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private val onUpdateClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val exerciseSets: TextView = itemView.findViewById(R.id.exerciseSets)
        val exerciseReps: TextView = itemView.findViewById(R.id.exerciseReps)
        val buttonUpdate: MaterialButton = itemView.findViewById(R.id.buttonUpdateExercise)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.exercise
        holder.exerciseSets.text = "Sets: ${exercise.sets}"
        holder.exerciseReps.text = "Reps: ${exercise.reps}"

        holder.buttonUpdate.setOnClickListener {
            onUpdateClick(exercise)
        }
    }

    override fun getItemCount() = exercises.size

    fun updateExercises(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    fun getExerciseAtPosition(position: Int): Exercise = exercises[position]
}
