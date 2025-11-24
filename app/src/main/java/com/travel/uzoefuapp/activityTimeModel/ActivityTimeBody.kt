package com.travel.uzoefuapp.activityTimeModel

import com.google.gson.annotations.SerializedName

class ActivityTimeBody(
    @SerializedName("activity_id") val activityId: String,
    @SerializedName("date") val date: String
)