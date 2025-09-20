package com.travel.uzoefuapp.updateProfileModel

import com.google.gson.annotations.SerializedName

class UpdateProfileBody(
    @SerializedName("first_name") val first_name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("dateofbirth") val dateofbirth: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("city") val city: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("category_id") val categoryIds: List<String>

)