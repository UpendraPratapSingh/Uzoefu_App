package com.travel.uzoefuapp.SearchActivityModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class SearchActivityResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {
        @SerializedName("activity_id")
        @Expose
        var activityId: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null
    }
}