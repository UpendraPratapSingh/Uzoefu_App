package com.travel.uzoefuapp.addTripModel

import com.google.gson.annotations.SerializedName

class AddTripBody(
    @SerializedName("title") val title: String,
    @SerializedName("destination") val destination: String
)
