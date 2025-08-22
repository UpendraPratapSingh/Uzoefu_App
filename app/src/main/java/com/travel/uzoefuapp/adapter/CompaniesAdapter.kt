package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class CompaniesAdapter(val context: Context) : RecyclerView.Adapter<CompaniesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.company_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 2
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) { }
}