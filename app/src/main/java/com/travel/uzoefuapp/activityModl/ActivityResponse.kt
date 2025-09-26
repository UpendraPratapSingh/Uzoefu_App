package com.travel.uzoefuapp.activityModl

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ActivityResponse : Serializable {
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

    @SerializedName("next_page_url")
    @Expose
    var nextPageUrl: String? = null

    @SerializedName("prev_page_url")
    @Expose
    var prevPageUrl: Any? = null

    @SerializedName("current_page")
    @Expose
    var currentPage: Int? = null

    @SerializedName("last_page")
    @Expose
    var lastPage: Int? = null

    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("activity_price")
        @Expose
        var activityPrice: String? = null

        @SerializedName("today_hours")
        @Expose
        var todayHours: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("rating_count")
        @Expose
        var ratingCount: Int? = null

        @SerializedName("is_wish")
        @Expose
        var isWish: Boolean? = null
    }
}