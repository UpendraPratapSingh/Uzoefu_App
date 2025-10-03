package com.travel.uzoefuapp.addTripModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AddTripResponse: Serializable {
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
        @SerializedName("trip")
        @Expose
        var trip: Trip? = null

        inner class Trip{
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("destination")
            @Expose
            var destination: String? = null

        }
    }
}