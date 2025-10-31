package com.travel.uzoefuapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.rewardHistoryModel.RewardHistoryResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class RewardRedeemAdapter(
    private val rewardList: List<RewardHistoryResponse.Datum>
) : RecyclerView.Adapter<RewardRedeemAdapter.RewardViewHolder>() {

    inner class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRewardTitle: TextView = itemView.findViewById(R.id.tvRewardTitle)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)

        //  val tvRedeemed: TextView = itemView.findViewById(R.id.tvRedeemed)
        val tvRedeemedDate: TextView = itemView.findViewById(R.id.tvRedeemedDate)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.redeem_reward_item, parent, false)
        return RewardViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewardList[position]

        holder.tvRewardTitle.text = reward.name
        holder.tvPoints.text = "+ ${reward.code} Points"
        val zonedDateTime = ZonedDateTime.parse(reward.createdAt)
        val formattedDate = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        holder.tvRedeemedDate.text = formattedDate
        holder.tvDescription.text = reward.description

    }

    override fun getItemCount(): Int = rewardList.size

}
