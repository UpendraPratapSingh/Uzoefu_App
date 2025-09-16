package com.travel.uzoefuapp.loginModel

import com.google.gson.annotations.SerializedName

class LoginBody(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)