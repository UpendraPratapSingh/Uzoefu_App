package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

data class FAQ(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)

class FAQAdapter(private val faqList: List<FAQ>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

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

        holder.tvAnswer.visibility = if (faq.isExpanded) View.VISIBLE else View.GONE
        holder.ivArrow.setImageResource(
            if (faq.isExpanded) R.drawable.baseline_remove else R.drawable.baseline_add_24
        )

        holder.faqLayout.setOnClickListener {
            faq.isExpanded = !faq.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = faqList.size
}