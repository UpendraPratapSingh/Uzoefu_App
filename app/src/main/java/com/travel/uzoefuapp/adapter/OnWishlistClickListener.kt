package com.travel.uzoefuapp.adapter

import com.travel.uzoefuapp.activityModl.ActivityResponse


interface OnWishlistClickListener {
    fun onWishlistClicked(product: ActivityResponse.Datum, position: Int)

}
