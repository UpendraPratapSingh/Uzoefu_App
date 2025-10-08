package com.travel.uzoefuapp.forgetPasswordModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ForgotPasswordResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data{
        @SerializedName("id")
        @Expose
        var id: Int? = null
    }
}