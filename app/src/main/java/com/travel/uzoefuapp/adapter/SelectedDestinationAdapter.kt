package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SelectDestinationActivity

class SelectedDestinationAdapter(val context: Context) : RecyclerView.Adapter<SelectedDestinationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selected_destination_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelectDestinationActivity::class.java)
            intent.putExtra("type", "1")
            intent.putExtra("Name", "Cape Town")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) { }
}