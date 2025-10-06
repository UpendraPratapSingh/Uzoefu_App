package com.travel.uzoefuapp.filterActivityModel

import com.google.gson.annotations.SerializedName

class FilterActivityBody(
    @SerializedName("province_id") val provinceId: String,
    @SerializedName("price") val price: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("category_id") val categoryId: String,
)