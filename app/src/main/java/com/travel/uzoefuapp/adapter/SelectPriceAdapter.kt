package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class SelectPriceAdapter(
    val context: Context
) : RecyclerView.Adapter<SelectPriceAdapter.ViewHolder>() {

    private val selectedFilters = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_price_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val price = "0 - 150"
        holder.bind(price, selectedFilters.contains(price))
    }

    override fun getItemCount(): Int = 4

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxPrice)

        fun bind(price: String, isSelected: Boolean) {
            checkBox.text = price

            checkBox.setOnCheckedChangeListener(null)

            checkBox.isChecked = isSelected

            checkBox.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    selectedFilters.add(price)
                } else {
                    selectedFilters.remove(price)
                }
            }
        }
    }
}
