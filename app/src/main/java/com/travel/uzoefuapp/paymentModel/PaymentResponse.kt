package com.travel.uzoefuapp.paymentModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PaymentResponse : Serializable {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("PAY_REQUEST_ID")
    @Expose
    var payRequestId: String? = null

    @SerializedName("CHECKSUM")
    @Expose
    var checksum: String? = null

    @SerializedName("process_url")
    @Expose
    var processUrl: String? = null
}