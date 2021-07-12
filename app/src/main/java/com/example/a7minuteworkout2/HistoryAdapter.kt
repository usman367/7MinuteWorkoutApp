package com.example.a7minuteworkout2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Creates an adapter class to bind the to RecyclerView to show the list of completed dates in History Screen.
class HistoryAdapter (val context: Context, val items: ArrayList<String>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>()  {


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //Gets the data from the item_history_row.xml
        // Holds the TextView that will add each item to
        val llHistoryItemMain = view.findViewById<LinearLayout>(R.id.ll_history_item_main)
        val tvItem = view.findViewById<TextView>(R.id.tvItem)
        val tvPosition = view.findViewById<TextView>(R.id.tvPosition)
    }

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //We want to use the item_history_row to present the data
        //We get the parent as a parameter from this method
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_history_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        //We get the items when we create an instance of this History adapter class (it's passed in as a parameter)
        return items.size
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //We get the date from whatever position we are in, in the items ArrayList
        val date: String = items.get(position)

        //+1 because we want't to start at 1 and not 0
        holder.tvPosition.text = (position + 1).toString()
        //Set the date TextView to the date we got from the ArrayList
        holder.tvItem.text = date

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.llHistoryItemMain.setBackgroundColor(
                Color.parseColor("#EBEBEB")
            )
        } else {
            holder.llHistoryItemMain.setBackgroundColor(
                Color.parseColor("#FFFFFF")
            )
        }
    }
}