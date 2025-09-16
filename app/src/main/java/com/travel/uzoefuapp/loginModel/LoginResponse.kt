package com.travel.uzoefuapp.loginModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class LoginResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("token")
        @Expose
        var token: String? = null

        @SerializedName("expires_at")
        @Expose
        var expiresAt: String? = null
    }
}