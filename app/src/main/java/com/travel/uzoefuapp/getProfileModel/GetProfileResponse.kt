package com.travel.uzoefuapp.getProfileModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class GetProfileResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null

    inner class Data {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("lastname")
        @Expose
        var lastname: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("dateofbirth")
        @Expose
        var dateofbirth: String? = null

        @SerializedName("mobile")
        @Expose
        var mobile: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("distance")
        @Expose
        var distance: String? = null

        @SerializedName("profile_photo_path")
        @Expose
        var profilePhotoPath: String? = null

        @SerializedName("category")
        @Expose
        var category: List<Category>? = null

        inner class Category {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("category_id")
            @Expose
            var categoryId: Int? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }
    }
}