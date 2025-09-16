package com.travel.uzoefuapp.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

data class NotificationItem(
    val type: String,   // "feature", "booking", "recommendation", "alert"
    val title: String,
    val message: String,
    val time: String
)

class NotificationAdapter(
    private val items: List<NotificationItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_ALERT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type == "alert") TYPE_ALERT else TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ALERT) {
            val view = inflater.inflate(R.layout.notification_alert, parent, false)
            AlertViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.notification_recyclerview, parent, false)
            NormalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is NormalViewHolder -> holder.bind(item)
            is AlertViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    // --------- ViewHolders ----------
    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.notificationText)
        private val timeText = itemView.findViewById<TextView>(R.id.timeText)

        fun bind(item: NotificationItem) {
           // title.text = "${item.title} : ${item.message}"
            val fullText = "${item.title} : ${item.message}"
            val spannable = SpannableString(fullText)

// make only the title part bold
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0, item.title.length, // start to end of title
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            title.text = spannable

            timeText.text = item.time
        }
    }

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tvFeatureMsg)
        private val message = itemView.findViewById<ImageView>(R.id.imgIcon)

        fun bind(item: NotificationItem) {
            title.text = item.message
        }
    }
}