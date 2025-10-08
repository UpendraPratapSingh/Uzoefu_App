package com.travel.uzoefuapp.notificationModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class NotificationSeenResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}