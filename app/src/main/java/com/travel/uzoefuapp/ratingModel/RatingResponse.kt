package com.travel.uzoefuapp.ratingModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RatingResponse: Serializable {
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

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("images")
        @Expose
        var images: List<String>? = null

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