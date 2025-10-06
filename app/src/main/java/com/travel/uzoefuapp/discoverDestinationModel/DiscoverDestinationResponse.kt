package com.travel.uzoefuapp.discoverDestinationModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DiscoverDestinationResponse : Serializable {
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
        @SerializedName("branch_id")
        @Expose
        var branchId: Int? = null

        @SerializedName("branch_name")
        @Expose
        var branchName: String? = null

        @SerializedName("activity_count")
        @Expose
        var activityCount: Int? = null

        @SerializedName("activity_image")
        @Expose
        var activityImage: String? = null

        @SerializedName("iswish")
        @Expose
        var iswish: Boolean? = null

    }
}