package com.travel.uzoefuapp.feedback

import com.google.gson.annotations.SerializedName

class FeedbackBody(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("message") val message: String
)
