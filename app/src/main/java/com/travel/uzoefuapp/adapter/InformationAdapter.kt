package com.travel.uzoefuapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

data class BusinessHour(val day: String, val time: String)

data class ExpandableItem(
    val title: String,
    val hours: List<BusinessHour>? = null,
    val content: String? = null,
    var isExpanded: Boolean = false
)

class InformationAdapter(private val items: List<ExpandableItem>, private val context: Context) :
    RecyclerView.Adapter<InformationAdapter.ExpandableViewHolder>() {

    inner class ExpandableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvIndicator: ImageView = itemView.findViewById(R.id.tvIndicator)
        val containerContent: LinearLayout = itemView.findViewById(R.id.containerContent)
        val faqLayout: LinearLayout = itemView.findViewById(R.id.faqLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expandable, parent, false)
        return ExpandableViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        val item = items[position]

        holder.tvTitle.text = item.title

        holder.tvIndicator.visibility = View.VISIBLE
        holder.tvIndicator.setImageResource(
            if (item.isExpanded) R.drawable.baseline_remove else R.drawable.baseline_add_24
        )

        holder.containerContent.removeAllViews()

        if (item.isExpanded) {
            holder.containerContent.visibility = View.VISIBLE

            item.hours?.forEach { hour ->
                val row = LayoutInflater.from(context)
                    .inflate(R.layout.item_business_hour, holder.containerContent, false)
                row.findViewById<TextView>(R.id.tvDay).text = hour.day
                row.findViewById<TextView>(R.id.tvTime).text = hour.time
                holder.containerContent.addView(row)
            }

            item.content?.let {
                val textView = TextView(context)
                textView.text = it
                textView.setTextColor(Color.DKGRAY)
                textView.textSize = 14f
                textView.setPadding(0, 8, 0, 0)
                holder.containerContent.addView(textView)
            }

        } else {
            holder.containerContent.visibility = View.GONE
        }

        holder.faqLayout.setOnClickListener {
            item.isExpanded = !item.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size
}