package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.companyActivities.BookingProductActivity

class WishlistAdapter(
    val context: Context,
    private val wishList: List<GetWishlistResponse.Datum>,
    private val onclickDeleteWishListListener: OnDeleteWishListListener
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    private var isEditMode = false
    private val selectedIds = mutableListOf<String>()

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
        holder.itemPrice.text = "R ${wishlistData.price.toString()}"
        holder.rateText.text = "(${wishlistData.ratingCount.toString()})"

        val imagePath = "https://mobappssolutions.in/uzoefu/public/images/activity_images/"

        // Click on item to open details OR toggle checkbox if in edit mode
        holder.itemView.setOnClickListener {
            if (!isEditMode) {
                val intent = Intent(context, BookingProductActivity::class.java)
                intent.putExtra("categoryId", wishlistData.activityId)
                context.startActivity(intent)
            } else {
                holder.deleteIcon.isChecked = !holder.deleteIcon.isChecked
            }
        }

        Glide.with(holder.itemView.context)
            .load(imagePath + wishlistData.image)
            .into(holder.itemImage)

        holder.deleteIcon.visibility = if (isEditMode) View.VISIBLE else View.GONE

        holder.deleteIcon.setOnCheckedChangeListener(null) // prevent recycling bug
        holder.deleteIcon.isChecked = selectedIds.contains(wishlistData.id.toString())

        holder.deleteIcon.setOnCheckedChangeListener { _, isChecked ->
            val id = wishlistData.id.toString()
            if (isChecked) {
                if (!selectedIds.contains(id)) selectedIds.add(id)
            } else {
                selectedIds.remove(id)
            }
            onclickDeleteWishListListener.onWishlistClicked(selectedIds, position)
        }
    }


    /*
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val wishlistData = wishList[position]
            holder.title.text = wishlistData.name
            holder.itemPrice.text = "R ${wishlistData.price.toString()}"
            holder.rateText.text = "(${wishlistData.ratingCount.toString()})"

            val imagePath = "https://mobappssolutions.in/uzoefu/public/images/activity_images/"

            holder.itemView.setOnClickListener {
                val checkBox = holder.deleteIcon
                checkBox.isChecked = !checkBox.isChecked
                wishlistData.id?.let { it1 ->
                    onclickDeleteWishListListener.onWishlistClicked(listOf(it1.toString()), position)
                }
            }

            holder.itemView.setOnClickListener {
                if (!isEditMode) {
                    // Only navigate to booking if NOT in edit mode
                    val intent = Intent(context, BookingProductActivity::class.java)
                    intent.putExtra("categoryId", wishlistData.activityId)
                    context.startActivity(intent)
                } else {
                    // Optional: show a message if user clicks in edit mode
                    val checkBox = holder.deleteIcon
                    checkBox.isChecked = !checkBox.isChecked
                    wishlistData.id?.let { it1 ->
                        onclickDeleteWishListListener.onWishlistClicked(
                            listOf(it1.toString()),
                            position
                        )
                    }
                }
            }

            Glide.with(holder.itemView.context)
                .load(imagePath + wishlistData.image)
                .into(holder.itemImage)
            holder.deleteIcon.visibility = if (isEditMode) View.VISIBLE else View.GONE

            holder.deleteIcon.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    wishlistData.id?.toString()?.let {
                        if (!selectedIds.contains(it)) {
                            selectedIds.add(it)
                        }
                    }
                } else {
                    selectedIds.remove(wishlistData.id.toString())
                }

                onclickDeleteWishListListener.onWishlistClicked(selectedIds, position)
            }

        }
    */

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
