package com.travel.uzoefuapp.ratingReviewModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RatingReviewResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("next_page_url")
    @Expose
    var nextPageUrl: Any? = null

    @SerializedName("prev_page_url")
    @Expose
    var prevPageUrl: Any? = null

    @SerializedName("current_page")
    @Expose
    var currentPage: Int? = null

    @SerializedName("last_page")
    @Expose
    var lastPage: Int? = null


    inner class Data {

        @SerializedName("current_page")
        @Expose
        var currentPage: Int? = null

        @SerializedName("data")
        @Expose
        var data: List<Datum>? = null

        @SerializedName("first_page_url")
        @Expose
        var firstPageUrl: String? = null

        @SerializedName("from")
        @Expose
        var from: Int? = null

        @SerializedName("last_page")
        @Expose
        var lastPage: Int? = null

        @SerializedName("last_page_url")
        @Expose
        var lastPageUrl: String? = null

        @SerializedName("links")
        @Expose
        var links: List<Link>? = null

        @SerializedName("next_page_url")
        @Expose
        var nextPageUrl: Any? = null

        @SerializedName("path")
        @Expose
        var path: String? = null

        @SerializedName("per_page")
        @Expose
        var perPage: Int? = null

        @SerializedName("prev_page_url")
        @Expose
        var prevPageUrl: Any? = null

        @SerializedName("to")
        @Expose
        var to: Int? = null

        @SerializedName("total")
        @Expose
        var total: Int? = null


        inner class Datum {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("rating")
            @Expose
            var rating: Int? = null

            @SerializedName("images")
            @Expose
            var images: List<String>? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("time_ago")
            @Expose
            var timeAgo: String? = null

            @SerializedName("activity_name")
            @Expose
            var activityName: String? = null

            @SerializedName("state_name")
            @Expose
            var stateName: String? = null

        }

        inner class Link {
            @SerializedName("url")
            @Expose
            var url: Any? = null

            @SerializedName("label")
            @Expose
            var label: String? = null

            @SerializedName("active")
            @Expose
            var active: Boolean? = null

        }

    }
}