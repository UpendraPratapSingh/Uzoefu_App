package com.travel.uzoefuapp.rewardHistoryModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RewardHistoryResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {
        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("points")
        @Expose
        var points: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
    }
}