package com.travel.uzoefuapp.bookingDetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.FragmentStep3Binding


class Step3Fragment : Fragment() {
    private var _binding: FragmentStep3Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStep3Binding.inflate(inflater, container, false)

        binding.signaturePad.post {
            binding.signaturePad.clear()
        }

        binding.signingCons.setOnClickListener {
            if (binding.addParticipantLayout.visibility == View.VISIBLE) {
                binding.addParticipantLayout.visibility = View.GONE
                binding.signingImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                binding.addParticipantLayout.visibility = View.VISIBLE
                binding.signingImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        binding.activityDescriptionCons.setOnClickListener {
            if (binding.addActivityLayout.visibility == View.VISIBLE) {
                binding.addActivityLayout.visibility = View.GONE
                binding.activityImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                binding.addActivityLayout.visibility = View.VISIBLE
                binding.activityImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        binding.agreementCons.setOnClickListener {
            if (binding.addAgreementLayout.visibility == View.VISIBLE) {
                binding.addAgreementLayout.visibility = View.GONE
                binding.agreementImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                binding.addAgreementLayout.visibility = View.VISIBLE
                binding.agreementImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        binding.indemnityCons.setOnClickListener {
            if (binding.addIndemnityLayout.visibility == View.VISIBLE) {
                binding.addIndemnityLayout.visibility = View.GONE
                binding.indemnityImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                binding.addIndemnityLayout.visibility = View.VISIBLE
                binding.indemnityImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        binding.declarationCons.setOnClickListener {
            if (binding.addDeclarationLayout.visibility == View.VISIBLE) {
                binding.addDeclarationLayout.visibility = View.GONE
                binding.declarationImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                binding.addDeclarationLayout.visibility = View.VISIBLE
                binding.declarationImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        binding.acknowledgementCons.setOnClickListener {
            if (binding.addAcknowledgementLayout.visibility == View.VISIBLE) {
                binding.addAcknowledgementLayout.visibility = View.GONE
                binding.acknowledgeImage.setImageResource(R.drawable.baseline_add_24)
            } else {
                binding.addAcknowledgementLayout.visibility = View.VISIBLE
                binding.acknowledgeImage.setImageResource(R.drawable.baseline_remove)
            }
        }

        return binding.root
    }
}