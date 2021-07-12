package com.example.a7minuteworkout2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        //Setting the action bar with the toolbar added in the activity layout and adding a back arrow button and its click event.
        val toolbar_finish_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_finish_activity)
        setSupportActionBar(toolbar_finish_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Same as
//        val actionbar = supportActionBar
//        if (actionbar != null){
//            actionbar.setDisplayHomeAsUpEnabled(true)
//        }

        //When the back button is pressed, go back
        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }


        //Adding a click event to the Finish Button.
        val btnFinish = findViewById<Button>(R.id.btnFinish)
        btnFinish.setOnClickListener {
            //It will finish this activity and go back to the activity underneath
            //Which is the start activity
            finish()
        }

        // Calling the function to insert the date into the database after we have finished the exercises
        addDateToDatabase()
    }


    // Creating the database and Inserting the date of Completion of the Exercise.
    /**
     * Function is used to insert the current system date in the sqlite database.
     */
    private fun addDateToDatabase() {

        val c = Calendar.getInstance() // Calendars Current Instance
        //Gets the current date and time from the calendar instance
        val dateTime = c.time // Current Date and Time of the system.
        Log.e("Date : ", "" + dateTime) // Printed in the log.

        /**
         * Here we have taken an instance of Date Formatter as it will format our
         * selected date in the format which we pass it as an parameter and Locale.
         * Here I have passed the format as dd MMM yyyy HH:mm:ss.
         *
         * The Locale : Gets the current value of the default locale for this instance
         * of the Java Virtual Machine.
         */
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()) // Date Formatter
        val date = sdf.format(dateTime) // dateTime is formatted in the given format.
        Log.e("Formatted Date : ", "" + date) // Formatted date is printed in the log.

        // Instance of the Sqlite Open Helper class.
        //We can pass in null for our factory because we made the parameter nullable
        val dbHandler = SqliteOpenHelper(this, null)

        //We're calling the addDate() function we created, and passing in the date as an argument
        dbHandler.addDate(date) // Add date function is called.
        Log.e("Date : ", "Added...") // Printed in log which is printed if the complete execution is done.
    }



}