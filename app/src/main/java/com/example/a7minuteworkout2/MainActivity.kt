package com.example.a7minuteworkout2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val llStart = findViewById<LinearLayout>(R.id.llstart)
        llStart.setOnClickListener {
            //When the button is clicked move to the exercise activity
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        // Adding a click event to the BMI calculator button and navigating it to the BMI calculator feature.
        val llBMI = findViewById<LinearLayout>(R.id.llBMI)
        llBMI.setOnClickListener {
            // Launching the BMI Activity
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        // Adding a click event to launch the History Screen Activity from Main Activity.
        val llHistory = findViewById<LinearLayout>(R.id.llHistory)
        llHistory.setOnClickListener {
            // Launching the History Activity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}