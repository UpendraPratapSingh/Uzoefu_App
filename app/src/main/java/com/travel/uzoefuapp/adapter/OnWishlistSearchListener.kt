package com.travel.uzoefuapp.adapter

import com.travel.uzoefuapp.filterActivityModel.FilterActivityResponse

interface OnWishlistSearchListener {
    fun onWishlistClick(product: FilterActivityResponse.Data.Datum, position: Int)
    fun onWishlistClicked(product: FilterActivityResponse.Data.Datum, position: Int)

}
