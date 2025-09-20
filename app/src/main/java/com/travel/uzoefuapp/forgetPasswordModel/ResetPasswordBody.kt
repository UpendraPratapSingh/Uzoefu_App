package com.travel.uzoefuapp.forgetPasswordModel

import com.google.gson.annotations.SerializedName

class ResetPasswordBody(
    @SerializedName("password")val password :String,
    @SerializedName("password_confirmation")val password_confirmation:String,
    @SerializedName("id")val id :String
)