package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.detailModel.DetailPageResponse


class FAQAdapter(private val faqList: List<DetailPageResponse.Data.Faq>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()


    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val faqLayout: LinearLayout = itemView.findViewById(R.id.faqLayout)
        val tvAnswer: TextView = itemView.findViewById(R.id.tvAnswer)
        val ivArrow: ImageView = itemView.findViewById(R.id.ivArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.faq_recyclerview, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faq = faqList[position]
        holder.tvQuestion.text = faq.question
        holder.tvAnswer.text = faq.answer

        val isExpanded = expandedPositions.contains(position)

        holder.tvAnswer.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.ivArrow.setImageResource(
            if (isExpanded) R.drawable.baseline_remove else R.drawable.baseline_add_24
        )

        holder.faqLayout.setOnClickListener {
            if (isExpanded) {
                expandedPositions.remove(position)
            } else {
                expandedPositions.add(position)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = faqList.size
}