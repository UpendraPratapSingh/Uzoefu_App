package com.travel.uzoefuapp.adapter

import com.travel.uzoefuapp.activityModl.ActivityResponse

interface OnWishlistListener {
    fun onWishlistClick(product: ActivityResponse.Datum, position: Int)
    fun onWishlistClicked(product: ActivityResponse.Datum, position: Int)

}