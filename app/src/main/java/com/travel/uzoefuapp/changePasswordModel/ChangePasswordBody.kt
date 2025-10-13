package com.travel.uzoefuapp.changePasswordModel

import com.google.gson.annotations.SerializedName

class ChangePasswordBody(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("confirm_password") val confirmPassword: String
)