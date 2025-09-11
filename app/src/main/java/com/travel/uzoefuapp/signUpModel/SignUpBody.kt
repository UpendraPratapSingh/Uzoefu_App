package com.travel.uzoefuapp.signUpModel

import com.google.gson.annotations.SerializedName

class SignUpBody(
    @SerializedName("contact_name") val contactName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)


