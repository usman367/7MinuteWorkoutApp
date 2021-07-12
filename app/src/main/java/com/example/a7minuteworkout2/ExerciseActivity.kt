package com.example.a7minuteworkout2

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null // Variable for Rest Timer and later on we will initialize it.
    private var restProgress = 0 // Variable for timer progress. As initial value the rest progress is set to 0. As we are about to start.

    private var exerciseTimer: CountDownTimer? = null // Variable for Exercise Timer and later on we will initialize it.
    private var exerciseProgress = 0 // Variable for the exercise timer progress. As initial value the exercise progress is set to 0. As we are about to start.

    // The Variable for the exercise list and current position of exercise here it is -1 as the list starting element is 0.
    private var exerciseList: ArrayList<ExerciseModel>? = null // We will initialize the list later.
    private var currentExercisePosition = -1 // Current Position of Exercise.

    private var tts: TextToSpeech? = null // Variable for Text to Speech
    //For the audio at the end of the exercises
    private var player: MediaPlayer? = null

    //Declaring a variable of an adapter class to bind it to recycler view
    //Initialized in the setupExerciseStatusRecyclerView() method
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        //TODO-Setting up the action bar using the toolbar and adding a back arrow button to it.-->
        val toolbar_exercise_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_exercise_activity)
        setSupportActionBar(toolbar_exercise_activity)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Same as:
        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        // Use the back pressed (close the activity) functionality when clicking the toolbar back button
        toolbar_exercise_activity.setNavigationOnClickListener {
            //This function does the same thing as clicking back on your phone
            //Goes back to the main activity when you press the toolbar
            //onBackPressed()

            //Now it shows the confirmation message using the custom dialog we created
            customDialogForBackButton()
        }

        //Context: this, we want to use the text to speech in this class
        //listener: this, we want to use this class as the listener as it already implements it
        tts = TextToSpeech(this, this)

        //The code below should be in that particular order
        //Because we need to get the list of exercise's first before starting the rest view
        //Because the rest view needs to display the next exercise's name

        ///We set the list to the method we created in the constants class, which returns a list of exercises
        exerciseList = Constants.defaultExerciseList()

        //Calling the function to make it visible on screen
        setupRestView() // REST View is set in this function

        setupExerciseStatusRecyclerView()
    }



    //TODO - Setting up the 10 seconds timer for rest view and updating it continuously.-->
    /**
     * Function is used to set the progress of timer using the progress
     */
    private fun setRestProgressBar() {

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = restProgress // Sets the current progress to the specified value.

        /**
         * @param millisInFuture The number of millis in the future from the call
         *   to {#start()} until the countdown is done and {#onFinish()}
         *   is called.
         * @param countDownInterval The interval along the way to receive
         *   {#onTick(long)} callbacks.
         */
        // Here we have started a timer of 10 seconds so the 10000 is milliseconds is 10 seconds and the countdown interval is 1 second so it 1000.
        //We're setting the rest timer to a countdown timer, it starts 1t 10s and goes down by a second
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //At every click the rest progress is increased by 1
                restProgress++ // It is increased by 1
                //The progress bar is updated
                progressBar.progress = 10 - restProgress // Indicates progress bar progress
                //The number in the middle of the ring is updated
                val tvTimer = findViewById<TextView>(R.id.tvTimer)
                tvTimer.text =
                    (10 - restProgress).toString()  // Current progress is set to text view in terms of seconds.
            }

            override fun onFinish() {
                //Make sure we have code in this order
                //Putting the setUpExerciseView() before incrementing the current position makes the app collapse
                //Probably because at the start the current position is at -1
                //So moving to the Exercise View doesn't work because we don't have a exercise at position -1
                //So we increment it to 0 before starting the exercise view

                //Every time the rest timer finishes we move to the next exercise
                //We're at position 0 at the start
                currentExercisePosition++

                //This is for the recycle view at the bottom
                //When we are getting an updated position of exercise set that item in the list as selected and notify the adapter class
                //setIsSelected() is a method in exercise model
                exerciseList!![currentExercisePosition].setIsSelected(true) // Current Item is selected
                //We want to tell the adapter the data has changed
                //It will only care about it if we tell it
                exerciseAdapter!!.notifyDataSetChanged() // Notified the current item to adapter class to reflect it into UI.

                // When the 10 seconds will complete this will be executed.
                //Then we want to show the exercise timer
               setupExerciseView()


            }
        }.start()
    }

    // TODO - After REST View Setting up the 30 seconds timer for the Exercise view and updating it continuously.
    /**
     * Function is used to set the progress of the timer using the progress for Exercise View for 30 Seconds
     */
    private fun setExerciseProgressBar() {

        val progressBarExercise = findViewById<ProgressBar>(R.id.progressBarExercise)
        progressBarExercise.progress = exerciseProgress

        //We want to start at 30s and countdown by 1s
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress = 30 - exerciseProgress
                val tvExerciseTimer = findViewById<TextView>(R.id.tvExerciseTimer)
                tvExerciseTimer.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {

                //After the countdown is over, if we still haven't completed all the exercises, start the rest timer again
                //if(currentExercisePosition < 1) { //For testing purposes to get to the finish activity faster

                if(currentExercisePosition < exerciseList?.size!! -1){
                    //Could be inside the if statement (above setupRestView()) (or above the if statement)
                    //This is for the recycle view at the bottom
                    //After we have completed this exercise un-select it and set it to completed
                    // We have changed the status of the selected item and updated the status of that, so that the position is set as completed in the exercise list.
                    exerciseList!![currentExercisePosition].setIsSelected(false) // exercise is completed so selection is set to false
                    exerciseList!![currentExercisePosition].setIsCompleted(true) // updating in the list that this exercise is completed
                    exerciseAdapter!!.notifyDataSetChanged() // Notifying the adapter class.


                    setupRestView()
                }else{
                    //Otherwise, switch to the finish activity
                    finish()
                    //Move over to the finish activity
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

                }
            }
        }.start()
    }


    //TODO - Setting up the Get Ready View with 10 seconds of timer.-->
    /**
     * Function is used to set the timer for REST.
     */
    private fun setupRestView() {

        // Playing a notification sound when the exercise is about to start when you are in the rest state
        //  the sound file is added in the raw folder (which we created) as resource.
        /**
         * Here the sound file is added in to "raw" folder in resources.
         * And played using MediaPlayer. MediaPlayer class can be used to control playback
         * of audio/video files and streams.
         */
        try {
            //val soundURI = Uri.parse("android.resource://com.example.a7minuteworkout2/" + R.raw.press_start)
            //player = MediaPlayer.create(applicationContext, soundURI)
            //Or just: (which is safer as you don't need to specify the path)
            player = MediaPlayer.create(applicationContext, R.raw.press_start)

            //Setting it to false so it only plays once
            player!!.isLooping = false // Sets the player to be looping or non-looping.
            player!!.start() // Starts Playback.
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //At the start of every rest view this is shown, and the exercise view is hidden
        //This is so when an exercise is finished, this screen is shown and the exercise screen is hidden
        val llRestView = findViewById<LinearLayout>(R.id.llRestView)
        val llExerciseView = findViewById<LinearLayout>(R.id.llExerciseView)
        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE

        /**
         * Here firstly we will check if the timer is running the and it is not null then cancel the running timer and start the new one.
         * And set the progress to initial which is 0.
         */
        if (restTimer != null) {
            //Cancel the rest timer
            //Set the rest progress to 0
            restTimer!!.cancel()
            restProgress = 0
        }

        // This function is used to set the progress details.
        setRestProgressBar()

        //This is for displaying the next exercise's name below the counter
        val tvUpcomingExerciseName = findViewById<TextView>(R.id.tvUpcomingExerciseName)
        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()
    }

    // TODO - Setting up the Exercise View with a 30 seconds timer.
    /**
     * Function is used to set the progress of the timer using the progress for Exercise View.
     */
    private fun setupExerciseView() {

        // Here according to the view make it visible as this is Exercise View so exercise view is visible and rest view is not.
        val llRestView = findViewById<LinearLayout>(R.id.llRestView)
        val llExerciseView = findViewById<LinearLayout>(R.id.llExerciseView)
        //We should'nt see the rest timer, but we should see the exercise timer now
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE

        /**
         * Here firstly we will check if the timer is running and it is not null then cancel the running timer and start the new one.
         * And set the progress to the initial value which is 0.
         */
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        //To speak the current exercise name
        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

        // Setting up the current exercise name and image to view to the UI element.
        val ivImage = findViewById<ImageView>(R.id.ivImage)
        val tvExerciseName = findViewById<TextView>(R.id.tvExerciseName)
        //Get the image at the current position from the array list we've created
        //We have the getImage() and getName() methods in the exerciseModel class
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        //Gets the name of the exercise at the current position from the array list we've created
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()


    }


    //TODO - Destroying the timer when closing the activity or app.-->
    /**
     * Here in the Destroy function we will reset the rest timer if it is running.
     */
    public override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        super.onDestroy()

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        //Shutting down the Text to Speech feature when activity is destroyed.
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player != null){
            player!!.stop()
        }

        super.onDestroy()
    }


    override fun onInit(status: Int) {
        //If we can access text to speech on the device
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            //If the language is not installed or not supported by the device
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String) {
        //The text we want it to speak
        //TextToSpeech.QUEUE_FLUSH, when the button is pressed the previous text being read is stopped and this is read instead
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    /**
     * Function is used to set up the recycler view to UI and assigning the Layout Manager and Adapter Class is attached to it.
     */
    //Binding adapter class to recycler view and setting the recycler view layout manager and passing a list to the adapter.
    private fun setupExerciseStatusRecyclerView() {

        // Defining a layout manager for the recycle view
        //We want to have the circles in a linear layout fashion
        // Here we have used a LinearLayout Manager with horizontal scroll.
        val rvExerciseStatus = findViewById<RecyclerView>(R.id.rvExerciseStatus)
        rvExerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // As the adapter expects the exercises list and context so initialize it passing it.
        //Initializing the variable we created at the top
        //We pass in the array list of exercise model and the context
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)

        // Adapter class is attached to recycler view
        rvExerciseStatus.adapter = exerciseAdapter
    }

    /**
     * Function is used to launch the custom confirmation dialog.
     */
    //Performing the steps to show the custom dialog for back button confirmation while the exercise is going on.
    private fun customDialogForBackButton() {
        //Creating the custom dialog
        val customDialog = Dialog(this)

        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        //Shows the custom dialog xml file we created on the screen
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)


        //customDialog.tvYes.setOnClickListener {//Doesn't work
        //We need to get the button first so (cant't do it separately):
        val tvYes = customDialog.findViewById<TextView>(R.id.tvYes).setOnClickListener {
            //If they confirm it then finish the activity
            finish()
            //Close the custom dialog
            customDialog.dismiss() // Dialog will be dismissed
        }

        //customDialog.tvNo.setOnClickListener {
        val tvNo = customDialog.findViewById<TextView>(R.id.tvNo).setOnClickListener {
            //If they pressed no, then just close the custom dialog
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }

}