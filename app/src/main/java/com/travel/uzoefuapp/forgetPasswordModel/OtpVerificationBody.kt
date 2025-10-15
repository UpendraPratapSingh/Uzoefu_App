package com.travel.uzoefuapp.forgetPasswordModel

import com.google.gson.annotations.SerializedName

class OtpVerificationBody(
    @SerializedName("otp") val otp: String,
    @SerializedName("user_id")val userId : String
)