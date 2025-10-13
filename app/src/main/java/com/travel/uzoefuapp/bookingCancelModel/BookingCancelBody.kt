package com.travel.uzoefuapp.bookingCancelModel

import com.google.gson.annotations.SerializedName

class BookingCancelBody(
    @SerializedName("booking_id") val bookingId: String
)