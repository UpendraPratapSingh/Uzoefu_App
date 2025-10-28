package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R


class RewardRedeemAdapter(
    private val rewardList: List<RewardItem>
) : RecyclerView.Adapter<RewardRedeemAdapter.RewardViewHolder>() {

    inner class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRewardTitle: TextView = itemView.findViewById(R.id.tvRewardTitle)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)

        //  val tvRedeemed: TextView = itemView.findViewById(R.id.tvRedeemed)
        //  val tvCode: TextView = itemView.findViewById(R.id.tvCode)

        val tvRedeemedDate: TextView = itemView.findViewById(R.id.tvRedeemedDate)
        val tvCodeCopied: TextView = itemView.findViewById(R.id.tvCodeCopied)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.redeem_reward_item, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewardList[position]

        holder.tvRewardTitle.text = reward.title
        holder.tvPoints.text = reward.points
        holder.tvRedeemedDate.text = "${reward.redeemedDate}"
        holder.tvCodeCopied.text = "${reward.confirmationCode}"

    }

    override fun getItemCount(): Int = rewardList.size

}

data class RewardItem(
    val title: String,
    val points: String,
    val redeemedDate: String,
    val confirmationCode: String
)
