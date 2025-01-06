package com.example.team_15_ctis_487

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.team_15_ctis_487.databinding.ActivityBmicheckingBinding

class BMICheckingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmicheckingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBmicheckingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bmi_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCalculateBMI.setOnClickListener {
            calculateBMI()
            binding.btnCalculateBMI.visibility = View.GONE
            binding.btnClose.visibility = View.VISIBLE
        }


        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun calculateBMI() {
        val weightText = binding.etWeight.text.toString()
        val heightText = binding.etHeight.text.toString()

        // Check for valid inputs
        if (weightText.isEmpty() || heightText.isEmpty()) {
            Toast.makeText(this, "Please enter both weight and height.", Toast.LENGTH_SHORT).show()
            return
        }

        val weight = weightText.toFloat()
        val height = heightText.toFloat()
        val heightInCM = height / 100

        // Calculate BMI
        val bmi = weight / (heightInCM * heightInCM)

        // Update UI
        binding.tvBMIResult.text = "%.2f".format(bmi)
        binding.seekBarBMI.progress = bmi.toInt()

        // Determine BMI category and update
        val bmiCategory = getBMICategory(bmi)
        binding.tvBMICategory.text = "$bmiCategory"
    }

    private fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }
    }
}