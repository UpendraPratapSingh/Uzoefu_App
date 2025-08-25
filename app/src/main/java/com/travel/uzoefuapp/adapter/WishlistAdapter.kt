package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class WishlistAdapter(val context: Context) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    private var isEditMode = false

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
        holder.title.text = "Birders Haven $position"

        holder.itemView.setOnClickListener {
            val checkBox = holder.deleteIcon
            checkBox.isChecked = !checkBox.isChecked

        }

        holder.deleteIcon.visibility = if (isEditMode) View.VISIBLE else View.GONE

        holder.deleteIcon.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val deleteIcon: CheckBox = itemView.findViewById(R.id.deleteIcon)
    }
}
