package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

data class BusinessFAQ(
    var question: String,
    var answer: String
)

class BusinessFaqAdapter(
    private val faqs: MutableList<BusinessFAQ>,
    private val onEditClick: (Int) -> Unit
) : RecyclerView.Adapter<BusinessFaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val editIcon: ImageView = itemView.findViewById(R.id.editIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.faq_item, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.tvQuestion.text = faqs[position].question
        holder.editIcon.setOnClickListener { onEditClick(position) }
    }

    override fun getItemCount() = faqs.size
}
