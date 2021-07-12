package com.example.a7minuteworkout2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager


class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val toolbar_history_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_history_activity)
        setSupportActionBar(toolbar_history_activity)

        val actionbar = supportActionBar //actionbar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true) //set back button
            actionbar.title = "HISTORY" // Setting a title in the action bar.
        }

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Calling a function for getting the list of completed dates when the History screen is launched.
        getAllCompletedDates()

    }

    //Created a function to get the list of completed dates from the History Table.
    /**
     * Function is used to get the list exercise completed dates.
     */
    private fun getAllCompletedDates() {
        val tvHistory = findViewById<TextView>(R.id.tvHistory)
        val rvHistory = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvHistory)
        val tvNoDataAvailable = findViewById<TextView>(R.id.tvNoDataAvailable)

        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)

        //Creates an ArrayList (automatically an ArrayList because the function we're gonna call returns an ArrayList)
        //Gets the list of all the dates in the database, using the function we created in the SqliteOpenHelper class
        val allCompletedDatesList = dbHandler.getAllCompletedDatesList() // List of history table data


        // We will pass that list to the adapter class which we have created and bind it to the recycler view.
        //If there are any entries in the database
        if (allCompletedDatesList.size > 0) {

            // Here if the List size is greater then 0 we will display the item in the recycler view or else we will show the text view that no data is available.
            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE

            // Creates a vertical Layout Manager
            rvHistory.layoutManager = LinearLayoutManager(this)

            // History adapter is initialized and the list is passed in the param.
            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)

            // Access the RecyclerView Adapter and load the data into it
            rvHistory.adapter = historyAdapter
        } else {
            //If we have no values in the ArrayList, we just want to show the text view with the appropriate message
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE
            tvNoDataAvailable.visibility = View.VISIBLE
        }
    }
}