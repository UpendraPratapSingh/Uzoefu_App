package com.travel.uzoefuapp.overviewModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class OverviewResponse: Serializable {
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
        @SerializedName("overview")
        @Expose
        var overview: Overview? = null

        inner class Overview{
            @SerializedName("wishlistcount")
            @Expose
            var wishlistcount: Int? = null

            @SerializedName("bookingcount")
            @Expose
            var bookingcount: Int? = null

            @SerializedName("reviewcount")
            @Expose
            var reviewcount: Int? = null

            @SerializedName("tripcount")
            @Expose
            var tripcount: Int? = null

            @SerializedName("visitedcount")
            @Expose
            var visitedcount: Int? = null

            @SerializedName("rewardcount")
            @Expose
            var rewardcount: Int? = null

            @SerializedName("photoscount")
            @Expose
            var photoscount: Int? = null
        }
    }
}