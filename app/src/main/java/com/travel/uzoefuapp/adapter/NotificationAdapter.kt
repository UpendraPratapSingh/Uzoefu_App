package com.travel.uzoefuapp.adapter

import android.app.AlertDialog
import android.content.Context
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
import com.travel.uzoefuapp.notificationModel.NotificationListResponse

class NotificationAdapter(
    private val context: Context,
    private var notificationList: List<NotificationListResponse.Datum>,
    private val notificationClickListener: NotificationOnClickListener,
    private val deleteListener: NotificationDeleteListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_recyclerview, parent, false)
        return NormalViewHolder(view, deleteListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = notificationList[position]
        val messTitle = item.title
        val mess = item.message
        val notiIdss = item.id.toString()

        when (holder) {
            is NormalViewHolder -> holder.bind(item)
            is AlertViewHolder -> holder.bind(item)

        }

        holder.itemView.setOnClickListener {
            showPopUpNotification(messTitle, mess, notiIdss)

            notificationClickListener.onNotificationClick(notiIdss)
        }

    }
    fun updateList(newList: List<NotificationListResponse.Datum>) {
        notificationList = newList
        notifyDataSetChanged()
    }

    private fun showPopUpNotification(heading: String?, message: String?, id: String?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.notification_layout, null)
        val builder = AlertDialog.Builder(context).setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_box)
        val closedialog = dialogView.findViewById<ImageView>(R.id.closedNotibox)
        val messTextTitle = dialogView.findViewById<TextView>(R.id.messTextTitle)
        val contentMessNoti = dialogView.findViewById<TextView>(R.id.contentMessNoti)
        messTextTitle.text = heading
        contentMessNoti.text = message
        closedialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun getItemCount(): Int = notificationList.size

    class NormalViewHolder(itemView: View,
                           private val deleteListener: NotificationDeleteListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.notificationText)
        private val notificationSee = itemView.findViewById<View>(R.id.notificationSeen)
        private val timeText = itemView.findViewById<TextView>(R.id.timeText)
        private val deleteNotification: ImageView = itemView.findViewById(R.id.deleteIcon)
        fun bind(item: NotificationListResponse.Datum) {
            val fullText = "${item.title} : ${item.message}"
            val spannable = SpannableString(fullText)

            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0, item.title?.length ?: 0,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            title.text = spannable
            timeText.text = item.timeAgo

            // Delete icon click
            deleteNotification.setOnClickListener {
                showDeletePopup(item)
            }

            // Hide or show the green dot based on isSeen
            if (item.isSeen == 1) {
                notificationSee.visibility = View.GONE
            } else {
                notificationSee.visibility = View.VISIBLE
            }
        }

        private fun showDeletePopup(item: NotificationListResponse.Datum) {
            AlertDialog.Builder(itemView.context)
                .setTitle("Delete Notification")
                .setMessage("Are you sure you want to delete this notification?")
                .setPositiveButton("Yes") { dialog, _ ->
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        deleteListener.onDeleteNotification(item.id.toString()) // now works
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tvFeatureMsg)
        private val icon = itemView.findViewById<ImageView>(R.id.imgIcon)

        fun bind(item: NotificationListResponse.Datum) {
            title.text = item.message
        }
    }
}
