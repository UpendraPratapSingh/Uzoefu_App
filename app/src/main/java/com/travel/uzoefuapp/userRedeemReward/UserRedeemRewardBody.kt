package com.travel.uzoefuapp.userRedeemReward

import com.google.gson.annotations.SerializedName

class UserRedeemRewardBody(
    @SerializedName("user_id") val userId: String,
    @SerializedName("reward_id") val rewardId: String
)