package com.travel.uzoefuapp.notificationModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class NotificationListResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null


    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("is_seen")
        @Expose
        var isSeen: Int? = null

        @SerializedName("time_ago")
        @Expose
        var timeAgo: String? = null
    }
}