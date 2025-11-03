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
        val tvPointsGet: TextView = itemView.findViewById(R.id.tvPointsGet)
        val tvRedeemedDate: TextView = itemView.findViewById(R.id.tvRedeemedDate)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvCodeCopied: TextView = itemView.findViewById(R.id.tvCodeCopied)
        val tvCode: TextView = itemView.findViewById(R.id.tvCode)
        val tvRedeemed: TextView = itemView.findViewById(R.id.tvRedeemed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.redeem_reward_item, parent, false)
        return RewardViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewardList[position]
        val type = reward.type.toString()

        if (type == "redeemed") {
            holder.tvPoints.visibility = View.GONE
            holder.tvPointsGet.visibility = View.VISIBLE
            holder.tvCode.visibility = View.VISIBLE
            holder.tvCodeCopied.visibility = View.VISIBLE
            holder.tvDescription.visibility = View.GONE
            holder.tvRewardTitle.visibility = View.VISIBLE
            holder.tvPointsGet.text = "- ${reward.points} Points"
            holder.tvRewardTitle.text = reward.name
            val zonedDateTime = ZonedDateTime.parse(reward.createdAt)
            val formattedDate = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            holder.tvRedeemedDate.text = formattedDate
            holder.tvCodeCopied.text = reward.code
            holder.tvRedeemed.text = "Redeemed • "

        } else if (type == "earned") {
            holder.tvPoints.visibility = View.VISIBLE
            holder.tvDescription.visibility = View.VISIBLE
            holder.tvPointsGet.visibility = View.GONE
            holder.tvRewardTitle.visibility = View.VISIBLE
            holder.tvCodeCopied.visibility = View.GONE
            holder.tvCode.visibility = View.GONE
            holder.tvPoints.text = "+ ${reward.points} Points"
            holder.tvRewardTitle.text = reward.name
            val zonedDateTime = ZonedDateTime.parse(reward.createdAt)
            val formattedDate = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            holder.tvRedeemedDate.text = formattedDate
            holder.tvDescription.text = reward.description
            holder.tvRedeemed.text = "Date • "
        }
    }

    override fun getItemCount(): Int = rewardList.size

}
