package com.travel.uzoefuapp.updateProfileModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateProfileResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("user")
        @Expose
        var user: User? = null

        @SerializedName("categories")
        @Expose
        var categories: List<String>? = null

        inner class User {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("lastname")
            @Expose
            var lastname: String? = null

            @SerializedName("username")
            @Expose
            var username: String? = null

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

        }
    }
}