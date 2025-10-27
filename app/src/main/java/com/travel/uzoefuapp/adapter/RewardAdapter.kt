package com.travel.uzoefuapp.adapter


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R


data class Reward(
    val title: String,
    val description: String,
    val pointsRequired: Int
)

class RewardAdapter(
    private val rewards: List<Reward>,
    private val context: Context,
    function: () -> Unit
) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    inner class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvPoints: TextView = itemView.findViewById(R.id.tvPoints)
        val btnRedeem: TextView = itemView.findViewById(R.id.btnRedeem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewards[position]
        holder.tvTitle.text = reward.title
        holder.tvDescription.text = reward.description
        holder.tvPoints.text = "${reward.pointsRequired} pts"
        holder.btnRedeem.setOnClickListener {
            showRedeemDialog(holder.itemView.context, reward)
        }
    }

    private fun showRedeemDialog(context: Context, reward: Reward) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_redeem_reward, null)

        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<TextView>(R.id.tvRewardTitle).text = reward.title
        view.findViewById<TextView>(R.id.tvRewardDesc).text = reward.description
        view.findViewById<TextView>(R.id.tvRewardPoints).text = "${reward.pointsRequired} Points"

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            Toast.makeText(context, "Reward Redeemed Successfully!", Toast.LENGTH_SHORT).show()
            showRewardRedeemedDialog("UZR124-300978-25", 50)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRewardRedeemedDialog(code: String, newBalance: Int) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_reward_redeemed, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(false)

        val tvCode = view.findViewById<TextView>(R.id.tvCode)
        val tvBalance = view.findViewById<TextView>(R.id.tvBalance)
        val btnDone = view.findViewById<Button>(R.id.btnDone)

        tvCode.text = code
        tvBalance.text = "$newBalance pts"

        btnDone.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun getItemCount(): Int {
        Log.d("Rewards", rewards.size.toString()) // should print 4

        return rewards.size
    }
}
