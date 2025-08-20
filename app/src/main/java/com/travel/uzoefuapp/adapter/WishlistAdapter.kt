package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class WishlistAdapter(val context: Context) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    // Flag for edit mode
    private var isEditMode = false

    // Function to toggle edit mode
    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wishlist_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Example: set dummy title (replace with your model data later)
        holder.title.text = "Birders Haven $position"

        // Show/hide delete icon based on edit mode
        holder.deleteIcon.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Handle delete click
        holder.deleteIcon.setOnClickListener {
            // TODO: remove item from your list (when you have a real data list)
        }
    }

    override fun getItemCount(): Int {
        return 6 // Replace with your actual list.size when using real data
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle) // your TextView in card
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon) // delete icon
    }
}
