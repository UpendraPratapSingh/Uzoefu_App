package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistResponse
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.R

class WishlistAdapter(val context: Context,
    private val wishList: List<GetWishlistResponse.Datum>
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

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
        val wishlistData = wishList[position]
        holder.title.text = wishlistData.name
        holder.itemPrice.text ="R ${wishlistData.price.toString()}"
        holder.rateText.text = wishlistData.ratingCount.toString()

        val imagePath = "https://mobappssolutions.in/uzoefu/public/images/activity_images/"

        holder.itemView.setOnClickListener {
            val checkBox = holder.deleteIcon
            checkBox.isChecked = !checkBox.isChecked

        }

        Glide.with(holder.itemView.context)
            .load(imagePath+wishlistData.image)
            .into(holder.itemImage)
        holder.deleteIcon.visibility = if (isEditMode) View.VISIBLE else View.GONE

        holder.deleteIcon.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val itemImage: ShapeableImageView = itemView.findViewById(R.id.itemImage)
        val deleteIcon: CheckBox = itemView.findViewById(R.id.deleteIcon)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val rateText: TextView = itemView.findViewById(R.id.rateText)
    }
}
