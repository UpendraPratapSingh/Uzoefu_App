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
    var data: Data? = null

    inner class Data{
        @SerializedName("mon_from")
        @Expose
        var monFrom: String? = null

        @SerializedName("mon_to")
        @Expose
        var monTo: String? = null

        @SerializedName("tue_from")
        @Expose
        var tueFrom: String? = null

        @SerializedName("tue_to")
        @Expose
        var tueTo: String? = null

        @SerializedName("wed_from")
        @Expose
        var wedFrom: String? = null

        @SerializedName("wed_to")
        @Expose
        var wedTo: String? = null

        @SerializedName("thu_from")
        @Expose
        var thuFrom: String? = null

        @SerializedName("thu_to")
        @Expose
        var thuTo: String? = null

        @SerializedName("fri_from")
        @Expose
        var friFrom: String? = null

        @SerializedName("fri_to")
        @Expose
        var friTo: String? = null

        @SerializedName("sat_from")
        @Expose
        var satFrom: String? = null

        @SerializedName("sat_to")
        @Expose
        var satTo: String? = null

        @SerializedName("sun_from")
        @Expose
        var sunFrom: String? = null

        @SerializedName("sun_to")
        @Expose
        var sunTo: String? = null
    }
}