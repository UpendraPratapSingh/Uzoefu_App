package com.travel.uzoefuapp.GetWishlistModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GetWishlistResponse : Serializable {
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
        var activityId: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("rating_count")
        @Expose
        var ratingCount: Int? = null
    }
}