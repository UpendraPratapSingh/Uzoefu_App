package com.travel.uzoefuapp.priceCalculationModel

import com.google.gson.annotations.SerializedName

class PriceCalculationBody(
    @SerializedName("activity_id") val activity_id: String,
    @SerializedName("date") val date: String,
    @SerializedName("adultcount") val adultcount: String,
    @SerializedName("kidscount") val kidscount: String,
)