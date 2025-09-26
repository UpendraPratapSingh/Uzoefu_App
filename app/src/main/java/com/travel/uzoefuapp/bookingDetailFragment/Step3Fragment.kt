package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.FragmentStep3Binding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step3Fragment(val activityId: String) : Fragment() {
    private var _binding: FragmentStep3Binding? = null
    private val binding get() = _binding!!
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStep3Binding.inflate(inflater, container, false)

        binding.signaturePad.post {
            binding.signaturePad.clear()
        }

        getDetailApi()
        getDetailObserver()

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

    private fun getDetailObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val description = response.peekContent().data?.description
            val agreement = response.peekContent().data?.indemnity

            if (success == true) {
                binding.secondSectionContent.text =description?.description.toString()
                binding.secondAgreementContent.text = agreement?.agreement.toString()
                binding.secondIndemnityContent.text = agreement?.waiverAndIndemnity.toString()
                binding.secondDeclarationContent.text = agreement?.declaration.toString()
                binding.secondAcknowledgementContent.text = agreement?.acknowledgement.toString()

            }
        }
        detailPageViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getDetailApi() {
        val body = DetailPageBody(
            activity_id = activityId
        )
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)

    }
}