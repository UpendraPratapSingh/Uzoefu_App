package com.travel.uzoefuapp.bookingCompleteModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BookingDetailResponse: Serializable {
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
        @SerializedName("booking_detail")
        @Expose
        var bookingDetail: BookingDetail? = null

        inner class BookingDetail{
            @SerializedName("booking_id")
            @Expose
            var bookingId: Int? = null

            @SerializedName("activity_name")
            @Expose
            var activityName: String? = null

            @SerializedName("booking_date")
            @Expose
            var bookingDate: String? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("payment_id")
            @Expose
            var paymentId: String? = null

            @SerializedName("contact_name")
            @Expose
            var contactName: String? = null

            @SerializedName("contact_number")
            @Expose
            var contactNumber: String? = null

            @SerializedName("email")
            @Expose
            var email: String? = null

            @SerializedName("billing_address")
            @Expose
            var billingAddress: String? = null

            @SerializedName("adult_qty")
            @Expose
            var adultQty: Int? = null

            @SerializedName("kids_qty")
            @Expose
            var kidsQty: Int? = null

            @SerializedName("adult_amount")
            @Expose
            var adultAmount: String? = null

            @SerializedName("kids_amount")
            @Expose
            var kidsAmount: String? = null

            @SerializedName("subtotal")
            @Expose
            var subtotal: String? = null

            @SerializedName("total")
            @Expose
            var total: String? = null
        }
    }
}