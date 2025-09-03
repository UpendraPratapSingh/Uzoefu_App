package com.travel.uzoefuapp.businessActivities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityBusinessTermAndConditionBinding

class BusinessTermAndConditionActivity : AppCompatActivity() {
    lateinit var binding: ActivityBusinessTermAndConditionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessTermAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        binding.radioGroupIndemnity.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioYes) {
                binding.tvConfigureIndemnity.visibility = View.VISIBLE
            } else {
                binding.tvConfigureIndemnity.visibility = View.GONE
            }
        }
        binding.tvConfigureIndemnity.setOnClickListener {
            showIndemnityBottomSheet()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showIndemnityBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_indemnity, null)
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = false
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val signingCons = view.findViewById<ConstraintLayout>(R.id.signingCons)
        val addParticipantLayout = view.findViewById<ConstraintLayout>(R.id.addParticipantLayout)
        val signingImage = view.findViewById<ImageView>(R.id.signingImage)

        val activityDescriptionCons = view.findViewById<ConstraintLayout>(R.id.activityDescriptionCons)
        val addActivityLayout = view.findViewById<ConstraintLayout>(R.id.addActivityLayout)
        val activityImage = view.findViewById<ImageView>(R.id.activityImage)

        val closeSheet = view.findViewById<ImageView>(R.id.closeSheet)

        val agreementCons = view.findViewById<ConstraintLayout>(R.id.agreementCons)
        val addAgreementLayout = view.findViewById<ConstraintLayout>(R.id.addAgreementLayout)
        val agreementImage = view.findViewById<ImageView>(R.id.agreementImage)

        val indemnityCons = view.findViewById<ConstraintLayout>(R.id.indemnityCons)
        val addIndemnityLayout = view.findViewById<ConstraintLayout>(R.id.addIndemnityLayout)
        val indemnityImage = view.findViewById<ImageView>(R.id.indemnityImage)

        val declarationCons = view.findViewById<ConstraintLayout>(R.id.declarationCons)
        val addDeclarationLayout = view.findViewById<ConstraintLayout>(R.id.addDeclarationLayout)
        val declarationImage = view.findViewById<ImageView>(R.id.declarationImage)

        val acknowledgementCons = view.findViewById<ConstraintLayout>(R.id.acknowledgementCons)
        val addAcknowledgementLayout =
            view.findViewById<ConstraintLayout>(R.id.addAcknowledgementLayout)
        val acknowledgeImage = view.findViewById<ImageView>(R.id.acknowledgeImage)

        signingCons.setOnClickListener {
            if (addParticipantLayout.visibility == View.VISIBLE) {
                addParticipantLayout.visibility = View.GONE
                signingImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                addParticipantLayout.visibility = View.VISIBLE
                signingImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        activityDescriptionCons.setOnClickListener {
            if (addActivityLayout.visibility == View.VISIBLE) {
                addActivityLayout.visibility = View.GONE
                activityImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                addActivityLayout.visibility = View.VISIBLE
                activityImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        agreementCons.setOnClickListener {
            if (addAgreementLayout.visibility == View.VISIBLE) {
                addAgreementLayout.visibility = View.GONE
                agreementImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                addAgreementLayout.visibility = View.VISIBLE
                agreementImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        indemnityCons.setOnClickListener {
            if (addIndemnityLayout.visibility == View.VISIBLE) {
                addIndemnityLayout.visibility = View.GONE
                indemnityImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                addIndemnityLayout.visibility = View.VISIBLE
                indemnityImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        declarationCons.setOnClickListener {
            if (addDeclarationLayout.visibility == View.VISIBLE) {
                addDeclarationLayout.visibility = View.GONE
                declarationImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                addDeclarationLayout.visibility = View.VISIBLE
                declarationImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        acknowledgementCons.setOnClickListener {
            if (addAcknowledgementLayout.visibility == View.VISIBLE) {
                addAcknowledgementLayout.visibility = View.GONE
                acknowledgeImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                addAcknowledgementLayout.visibility = View.VISIBLE
                acknowledgeImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        closeSheet.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}