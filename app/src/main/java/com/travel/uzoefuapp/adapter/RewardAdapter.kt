package com.travel.uzoefuapp.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.redeemRewardModel.RewardRedeemResponse


data class Reward(val title: String, val description: String, val pointsRequired: Int)

class RewardAdapter(
    private val rewards: List<RewardRedeemResponse.Datum>,
    private val context: Context,
    val currentBalance: String,
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
        holder.tvTitle.text = reward.name
        holder.tvDescription.text = reward.description
        holder.tvPoints.text = "${reward.points} pts"
        holder.btnRedeem.setOnClickListener {
            showRedeemDialog(holder.itemView.context, reward)
        }
    }

    private fun showRedeemDialog(context: Context, reward: RewardRedeemResponse.Datum) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_redeem_reward, null)

        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<TextView>(R.id.tvRewardTitle).text = reward.name
        view.findViewById<TextView>(R.id.tvRewardDesc).text = reward.description
        view.findViewById<TextView>(R.id.tvRewardPoints).text = "${reward.points} Points"
        view.findViewById<TextView>(R.id.tvRedeemPoints).text = "-${reward.points} pts"
        view.findViewById<TextView>(R.id.tvCurrentBalance).text = "${currentBalance} pts"

        val rewardPointsStr: String = reward.points.toString()
        val currentBalanceStr: String = currentBalance

        val rewardPoints = rewardPointsStr.toIntOrNull() ?: 0
        val currentBalanceVal = currentBalanceStr.toIntOrNull() ?: 0

        val newBalance = (currentBalanceVal - rewardPoints).coerceAtLeast(0)

        Log.d("Balance", "New Balance: $newBalance")

        view.findViewById<TextView>(R.id.tvNewBalance).text = newBalance.toString()

        val code = reward.code

        view.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<ImageView>(R.id.closeIcon).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.btnConfirm).setOnClickListener {
            Toast.makeText(context, "Reward Redeemed Successfully!", Toast.LENGTH_SHORT).show()
            if (code != null) {
                showRewardRedeemedDialog(code, 50)
            }
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
        val btnDone = view.findViewById<TextView>(R.id.btnDone)
        val closeBtn = view.findViewById<ImageView>(R.id.closeIcon)
        val copyCode = view.findViewById<ImageView>(R.id.imgCopy)

        tvCode.text = code
        tvBalance.text = "$newBalance pts"

        copyCode.setOnClickListener {
            val codeText = tvCode.text.toString()
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            clipboard?.let {
                val clip = ClipData.newPlainText("Confirmation Code", codeText)
                it.setPrimaryClip(clip)
                Toast.makeText(context, "Code copied!", Toast.LENGTH_SHORT).show()
            }
        }
        btnDone.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    override fun getItemCount(): Int {
        return rewards.size
    }
}
