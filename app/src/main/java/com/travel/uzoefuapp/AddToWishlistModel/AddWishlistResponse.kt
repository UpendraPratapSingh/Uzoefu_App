package com.travel.uzoefuapp.AddToWishlistModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AddWishlistResponse : Serializable {
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
        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("activity_id")
        @Expose
        var activityId: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null
    }
}