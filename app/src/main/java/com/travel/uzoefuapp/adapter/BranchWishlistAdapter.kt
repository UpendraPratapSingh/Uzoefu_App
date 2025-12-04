package com.travel.uzoefuapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SelectDestinationActivity
import com.travel.uzoefuapp.branchWishlistModel.GetWishlistResponse

class BranchWishlistAdapter(
    val context: Context,
    private val wishList: List<GetWishlistResponse.Datum>,
    private val onclickDeleteWishListListener: OnDeleteWishListListener
) : RecyclerView.Adapter<BranchWishlistAdapter.ViewHolder>() {
    private var isEditMode = false
    private val selectedIds = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.branch_wishlist_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wishlistData = wishList[position]
        holder.title.text = "${wishlistData.branchName} (Activities ${wishlistData.activityCount})"
        // Set the rating bar

        // Set progress/number (assuming you have a TextView)
        //  holder.tvRatingProgress.text = rating.toInt().toString()

        val imagePath = "https://uzoefu.co.za/public/images/activity_images/"

        // Click on item to open details OR toggle checkbox if in edit mode
        holder.itemView.setOnClickListener {
            if (!isEditMode) {
                val intent = Intent(context, SelectDestinationActivity::class.java)
                intent.putExtra(
                    "categoryName",
                    "${wishlistData.branchName} (${wishlistData.activityCount})"
                )
                intent.putExtra("branchId", wishlistData.branchId.toString())
                context.startActivity(intent)
            } else {
                holder.deleteIcon.isChecked = !holder.deleteIcon.isChecked
            }
        }

        /*
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, SelectDestinationActivity::class.java)
                    intent.putExtra(
                        "categoryName",
                        "${discoverList.branchName} (${discoverList.activityCount})"
                    )
                    intent.putExtra("branchId", discoverList.branchId.toString())
                    context.startActivity(intent)
                }
        */

        Glide.with(holder.itemView.context)
            .load(imagePath + wishlistData.activityImage)
            .into(holder.itemImage)

        holder.deleteIcon.visibility = if (isEditMode) View.VISIBLE else View.GONE

        holder.deleteIcon.setOnCheckedChangeListener(null)
        holder.deleteIcon.isChecked = selectedIds.contains(wishlistData.wishlistId.toString())

        holder.deleteIcon.setOnCheckedChangeListener { _, isChecked ->
            val id = wishlistData.wishlistId.toString()
            if (isChecked) {
                if (!selectedIds.contains(id)) selectedIds.add(id)
            } else {
                selectedIds.remove(id)
            }
            onclickDeleteWishListListener.onWishlistClicked(selectedIds, position)
        }
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val itemImage: ShapeableImageView = itemView.findViewById(R.id.itemImage)
        val deleteIcon: CheckBox = itemView.findViewById(R.id.deleteIcon)
    }
}
