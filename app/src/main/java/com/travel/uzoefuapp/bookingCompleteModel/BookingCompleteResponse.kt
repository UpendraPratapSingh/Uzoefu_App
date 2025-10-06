package com.travel.uzoefuapp.bookingCompleteModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BookingCompleteResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("image_path")
    @Expose
    var imagePath: String? = null

    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("activity_id")
        @Expose
        var activityId: Int? = null

        @SerializedName("activity_name")
        @Expose
        var activityName: String? = null

        @SerializedName("images")
        @Expose
        var images: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

        @SerializedName("date")
        @Expose
        var date: String? = null

        @SerializedName("activity_status")
        @Expose
        var activityStatus: String? = null
    }
}