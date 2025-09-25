package com.travel.uzoefuapp.priceCalculationModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PriceCalculationResponse : Serializable {
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
        @SerializedName("prcing_detail")
        @Expose
        var prcingDetail: PrcingDetail? = null

        inner class PrcingDetail {
            @SerializedName("date")
            @Expose
            var date: String? = null

            @SerializedName("adult")
            @Expose
            var adult: Int? = null

            @SerializedName("kids")
            @Expose
            var kids: Int? = null

            @SerializedName("adult_count")
            @Expose
            var adultCount: String? = null

            @SerializedName("kids_count")
            @Expose
            var kidsCount: String? = null

            @SerializedName("subtotal")
            @Expose
            var subtotal: String? = null

            @SerializedName("total")
            @Expose
            var total: String? = null

        }
    }
}