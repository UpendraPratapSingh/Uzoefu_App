package com.travel.uzoefuapp.categoryModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CategoryResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("icon_url")
    @Expose
    var iconUrl: String? = null


    inner class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("icon")
        @Expose
        var icon: String? = null

        @SerializedName("activities_count")
        @Expose
        var activitiesCount: Int? = null
    }
}