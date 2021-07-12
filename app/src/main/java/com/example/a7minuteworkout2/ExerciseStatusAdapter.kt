package com.example.a7minuteworkout2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
//You need this adapter for recycle view
//It inherits from RecyclerView.Adapter
//We pass our own view holder as a parameter (which we created in this class) into the RecyclerView.Adapter
//We want to display the items which are an array list of type Exercise Model

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>, val context: Context) : RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    //Creating our own view holder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        //val tvItem = view.tvItem!!
        //The code above doesn't work because we need to get the tvItem first from item_exercise_status
        //Therefore just get it and do view. before it
        val tvItem = view.findViewById<TextView>(R.id.tvItem)

    }

    /**
     * Inflates the item view which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //We return the item_exercise_status as our view holder
        ///Displays it on the screen
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_exercise_status,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    //We want to assign the right text to everyone of those circles
    //We can adjust the look of every single element in our list here
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //We create a variable of type Exercise model, and pass the position of the items that are passed in
        //items is the parameter (array list of exercise model) passed in to the class above
        val model: ExerciseModel = items[position]

        //We set the text of our tvItem as the id of the exercise we're currently at
        holder.tvItem.text = model.getId().toString()

        // Updating the current item and the completed item in the UI and changing the background and text color according to it..
        // Updating the background and text color according to the flags that is in the list.
        // A link to set text color programmatically and same way we can set the drawable background also instead of color.
        // https://stackoverflow.com/questions/8472349/how-to-set-text-color-to-a-text-view-programmatically
        if (model.getIsSelected()) {
            //Then set the circle to the item_circular_thin_color_accent_border file
            holder.tvItem.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.item_circular_thin_color_accent_border
                )
            //Changes the color of the text
            holder.tvItem.setTextColor(Color.parseColor("#212121")) // Parse the color string, and return the corresponding color-int.
        } else if (model.getIsCompleted()) {
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_color_accent_background)
            //Text color is set to white
            holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            //Make the circle gray
            holder.tvItem.background =
                ContextCompat.getDrawable(context, R.drawable.item_circular_color_gray_background)
            holder.tvItem.setTextColor(Color.parseColor("#212121"))
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }


}