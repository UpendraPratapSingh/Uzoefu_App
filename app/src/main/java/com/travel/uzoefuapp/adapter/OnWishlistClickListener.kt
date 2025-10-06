package com.travel.uzoefuapp.adapter

import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse


interface OnWishlistClickListener {
    fun onWishlistClicked(product: ActivityResponse.Datum, position: Int)
    fun onWishlistDestinationClicked(product: DiscoverDestinationResponse.Datum, position: Int)

}
