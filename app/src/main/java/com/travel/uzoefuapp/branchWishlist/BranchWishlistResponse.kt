package com.travel.uzoefuapp.branchWishlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BranchWishlistResponse : Serializable {
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
        @SerializedName("branch_id")
        @Expose
        var branchId: Int? = null

        @SerializedName("branch_name")
        @Expose
        var branchName: String? = null

        @SerializedName("activity_image")
        @Expose
        var activityImage: String? = null
    }
}