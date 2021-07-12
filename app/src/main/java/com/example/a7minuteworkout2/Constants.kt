package com.example.a7minuteworkout2

import java.util.ArrayList

/**
 * Constant Class where you can add the constant values of the project.
 */
// TODO - Making a default list of exercises in the Constant file along with the name image using the Model Class which later on will be used to show in the UI.
class Constants {

    //We create a companion object so we have all these variable available throughout the whole project without making a object of the constants class
    //companion is the same as static in java
    companion object {

        // The drawable images used here are added in the drawable folder.
        /**
         * Here we are adding all exercises to a single list with all the default values.
         */
        //This function returns an array list of the exercise model class we created
        //The array list contains all the exercises
        fun defaultExerciseList(): ArrayList<ExerciseModel> {

            //Contains all the exercises
            val exerciseList = ArrayList<ExerciseModel>()

            //We create an exercise object
            val jumpingJacks = ExerciseModel(1, "Jumping Jacks", R.drawable.ic_jumping_jacks, false, false)
            //Then we add it to the array list
            exerciseList.add(jumpingJacks)

            val wallSit = ExerciseModel(2, "Wall Sit", R.drawable.ic_wall_sit, false, false)
            exerciseList.add(wallSit)

            val pushUp = ExerciseModel(3, "Push Up", R.drawable.ic_push_up, false, false)
            exerciseList.add(pushUp)

            val abdominalCrunch = ExerciseModel(4, "Abdominal Crunch", R.drawable.ic_abdominal_crunch, false, false)
            exerciseList.add(abdominalCrunch)

            val stepUpOnChair = ExerciseModel(5, "Step-Up onto Chair", R.drawable.ic_step_up_onto_chair, false, false)
            exerciseList.add(stepUpOnChair)

            val squat = ExerciseModel(6, "Squat", R.drawable.ic_squat, false, false)
            exerciseList.add(squat)

            val tricepDipOnChair = ExerciseModel(7, "Tricep Dip On Chair",R.drawable.ic_triceps_dip_on_chair, false, false)
            exerciseList.add(tricepDipOnChair)

            val plank = ExerciseModel(8, "Plank", R.drawable.ic_plank, false, false)
            exerciseList.add(plank)

            val highKneesRunningInPlace =
                ExerciseModel(9, "High Knees Running In Place", R.drawable.ic_high_knees_running_in_place, false, false)
            exerciseList.add(highKneesRunningInPlace)

            val lunges = ExerciseModel(10, "Lunges", R.drawable.ic_lunge, false, false)
            exerciseList.add(lunges)

            val pushupAndRotation = ExerciseModel(11, "Push up and Rotation", R.drawable.ic_push_up_and_rotation, false, false)
            exerciseList.add(pushupAndRotation)

            val sidePlank = ExerciseModel(12, "Side Plank", R.drawable.ic_side_plank, false, false)
            exerciseList.add(sidePlank)

            return exerciseList
        }
    }
}