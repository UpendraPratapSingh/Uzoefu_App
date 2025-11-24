package com.travel.uzoefuapp.activityTimeModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ActivityTimeResponse: Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null


    inner class Datum{
        @SerializedName("time")
        @Expose
        var time: String? = null

        @SerializedName("available")
        @Expose
        var available: Int? = null
    }
}