package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.FragmentStep3Binding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import android.util.Base64
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

data class Participant(
    val clientName: String,
    val idNumber: String,
    val contactNumber: String,
    val dateSigned: String,
    val signatureBase64: String
)

@AndroidEntryPoint
class Step3Fragment(val activityId: String, val booking: String) : Fragment() {
    private var _binding: FragmentStep3Binding? = null
    private val binding get() = _binding!!
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private val participants = mutableListOf<Participant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep3Binding.inflate(inflater, container, false)

        //  clearPreviousParticipants()

        val savedParticipants = getParticipantsFromPrefs()

        val pref = requireContext().getSharedPreferences("profile_pref", Context.MODE_PRIVATE)
        val prefs = requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)

        val sharedPref = requireContext().getSharedPreferences("booking_pref", Context.MODE_PRIVATE)
        val savedDate = sharedPref.getString("date", "")
        binding.etDateSigned.setText(savedDate)

        val username = pref.getString("username", "")
        val mobile = pref.getString("mobile", "")

        binding.etClientName.setText(username)
        binding.etContactNumber.setText(mobile)

        if (savedParticipants.isEmpty()) {
            clearPreviousParticipants()
            Toast.makeText(requireContext(), "Please fill participant details", Toast.LENGTH_LONG)
                .show()
        } else {
            val lastParticipant = savedParticipants.last()

            binding.etClientName.setText(lastParticipant.clientName)
            binding.etIdNumber.setText(lastParticipant.idNumber)
            binding.etContactNumber.setText(lastParticipant.contactNumber)
            binding.etDateSigned.setText(lastParticipant.dateSigned)

            val bitmap = base64ToBitmap(lastParticipant.signatureBase64)
            binding.signaturePad.clear()
            binding.signaturePad.post {
                binding.signaturePad.signatureBitmap = bitmap
            }
            participants.clear()
            participants.addAll(savedParticipants)
        }

        binding.signaturePad.post {
            binding.signaturePad.clear()
        }

        binding.btnClearSignature.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.tvAddParticipant.setOnClickListener { addParticipant() }

        binding.etDateSigned.setOnClickListener {
            showDatePicker(binding.etDateSigned)
        }

        //call api and observer
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

    private fun clearPreviousParticipants() {
        val sharedPref = requireContext().getSharedPreferences("participants_pref", 0)
        sharedPref.edit().clear().apply()
        participants.clear()
    }

    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun addParticipant() {
        val clientName = binding.etClientName.text.toString()
        val idNumber = binding.etIdNumber.text.toString()
        val contactNumber = binding.etContactNumber.text.toString()
        val dateSigned = binding.etDateSigned.text.toString()
        val signatureBitmap = binding.signaturePad.signatureBitmap

        if (clientName.isBlank() || idNumber.isBlank() || contactNumber.isBlank() || dateSigned.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val signatureBase64 = bitmapToBase64(signatureBitmap)
        val participant =
            Participant(clientName, idNumber, contactNumber, dateSigned, signatureBase64)
        participants.add(participant)

        saveParticipantsToPrefs()

        binding.etClientName.text?.clear()
        binding.etIdNumber.text?.clear()
        binding.etContactNumber.text?.clear()
        binding.etDateSigned.text?.clear()
        binding.signaturePad.clear()

        Toast.makeText(requireContext(), "Participant added", Toast.LENGTH_SHORT).show()
    }

    private fun saveParticipantsToPrefs() {
        val sharedPref = requireContext().getSharedPreferences("participants_pref", 0)
        val gson = Gson()
        val json = gson.toJson(participants)

        with(sharedPref.edit()) {
            putString("participants_list", json)
            apply()
        }
    }

    fun saveCurrentParticipantIfNotEmpty() {
        val clientName = binding.etClientName.text.toString()
        val idNumber = binding.etIdNumber.text.toString()
        val contactNumber = binding.etContactNumber.text.toString()
        val dateSigned = binding.etDateSigned.text.toString()
        val signatureBitmap = binding.signaturePad.signatureBitmap

        if (clientName.isNotBlank() || idNumber.isNotBlank() || contactNumber.isNotBlank() || dateSigned.isNotBlank()) {
            val signatureBase64 = bitmapToBase64(signatureBitmap)
            val participant =
                Participant(clientName, idNumber, contactNumber, dateSigned, signatureBase64)
            participants.add(participant)
            saveParticipantsToPrefs()


            binding.etClientName.text?.clear()
            binding.etIdNumber.text?.clear()
            binding.etContactNumber.text?.clear()
            binding.etDateSigned.text?.clear()
            binding.signaturePad.clear()
        }
    }

    private fun getParticipantsFromPrefs(): MutableList<Participant> {
        val sharedPref = requireContext().getSharedPreferences("participants_pref", 0)
        val gson = Gson()
        val json = sharedPref.getString("participants_list", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Participant>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val resizedBitmap = resizeBitmap(bitmap, 500, 500)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight

        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }

    private fun base64ToBitmap(base64: String): Bitmap {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun getDetailObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {}

        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val description = response.peekContent().data?.description
            val agreement = response.peekContent().data?.indemnity

            if (success == true) {
                binding.secondSectionContent.text = description?.description.toString()
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
        val body = DetailPageBody(activity_id = activityId)
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
