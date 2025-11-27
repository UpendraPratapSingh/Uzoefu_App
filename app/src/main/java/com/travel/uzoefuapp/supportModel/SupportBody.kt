package com.travel.uzoefuapp.supportModel

import com.google.gson.annotations.SerializedName

class SupportBody(
    @SerializedName("firstname") val firstname: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email_address") val email_address: String,
    @SerializedName("number") val number: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("comment") val comment: String,
)