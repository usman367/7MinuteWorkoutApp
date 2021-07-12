package com.example.a7minuteworkout2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList


/**
 * Create a helper object to create, open, and/or manage a database.
 * This method always returns very quickly.  The database is not actually
 * created or opened until one of getWritableDatabase or
 * getReadableDatabase is called.
 *
 * @param context to use for locating paths to the the database
 * @param name of the database file, or null for an in-memory database
 * @param factory to use for creating cursor objects, or null for the default
 * @param version number of the database (starting at 1); if the database is older,
 *     #onUpgrade will be used to upgrade the database; if the database is
 *     newer, #onDowngrade will be used to downgrade the database
 */
class SqliteOpenHelper (context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper (context, DATABASE_NAME, factory, DATABASE_VERSION) {

    //Storing static variables
    companion object {
        private const val DATABASE_VERSION = 1 // This DATABASE Version
        private const val DATABASE_NAME = "SevenMinutesWorkout.db" // Name of the DATABASE
        private const val TABLE_HISTORY = "history" // Table Name
        private const val COLUMN_ID = "_id" // Column Id
        private const val COLUMN_COMPLETED_DATE = "completed_date" // Column for Completed Date
    }

    /**
     * This override function is used to execute when the class is called once where the database tables are created.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        //Create table + we give the name of the table
        //After the "(" we give the name of the first column, and its constraints ("INTEGER PRIMARY KEY" with a comma at the end)
        //Then we give the name of the second column and its data type (text)
        val CREATE_HISTORY_TABLE = ("CREATE TABLE " +
                TABLE_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_COMPLETED_DATE
                + " TEXT)") // Create History Table Query.
        //If there is a database, execute the code, otherwise, do nothing
        db?.execSQL(CREATE_HISTORY_TABLE) // Executing the create table query.
    }

    /**
     * This function is called when the database version is changed.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //We drop the table (deleting it)
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY") // It drops the existing history table
        //Then recreating it with the latest version
        //By calling the onCreate() function
        onCreate(db) // Calls the onCreate function so all the updated tables will be created.
    }

    // Creating a function where the passed Date will be inserted into the Database Table.
    /**
     * Function is used to insert the date in Database History table.
     */
    fun addDate(date: String) {
        //Allows you to put a bunch of values into ContentValues(), and then write them into the database
        val values = ContentValues() // Creates an empty set of values using the default initial size

        //We want to store whatever date is passed in, into our column called "COLUMN_COMPLETED_DATE"
        values.put(COLUMN_COMPLETED_DATE, date) // Putting the value to the column along with the value.

        //Create a new writeable database
        val db = this.writableDatabase // Create and/or open a database that will be used for reading and writing.

        //First we pass in the table name, then null
        //And then the actual values you wnat to pass in
        db.insert(TABLE_HISTORY, null, values) // Insert query is return

        db.close() // Database is closed after insertion.
    }


    //Getting the list of completed dates from the History Table.
    /**
     * Function returns the list of history table data.
     */
    fun getAllCompletedDatesList(): ArrayList<String> {
        val list = ArrayList<String>() // ArrayList is initialized

        //We create a new readable database object
        val db = this.readableDatabase // Create and/or open a database that will be used for reading and writing.
        //  Runs the provided SQL and returns a Cursor over the result set.

        // Query for selecting all the data from history table.
        val cursor = db.rawQuery("SELECT * FROM $TABLE_HISTORY", null)

        // Move the cursor to the next row.
        while (cursor.moveToNext()) {//Moves to the next row,as long as there is an entry in the database
            // Returns the zero-based index for the given column name, or -1 if the column doesn't exist.
            //Gets the data from the column called COLUMN_COMPLETED_DATE, and returns it as a string
            //And then it adds it our ArrayList
            list.add(cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETED_DATE))) // value is added in the list
        }
        cursor.close() // Cursor is closed after its used.
        return list // List is returned.
    }


}