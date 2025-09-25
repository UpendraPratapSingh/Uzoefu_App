package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.databinding.FragmentStep1Binding
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationBody
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class Step1Fragment(val price: String, private val childrenPrice: String, val activityId: String) :
    Fragment() {
    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!
    private val priceCalculationViewModel: PriceCalculationViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private var adultCount = 1
    private var kidCount = 1
    private var selectedDate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStep1Binding.inflate(inflater, container, false)

        binding.datePicker.setOnClickListener { datePickerCode() }

        // 👇 Set current date as default
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        selectedDate = String.format("%04d-%02d-%02d", year, month, day)
        binding.tvDate.text = selectedDate
        saveActivityIdLocally(activityId)
        // priceCalculateApi()
        priceCalculateObserver()
        priceCalculateApi()

        binding.btnPlusAdult.setOnClickListener {
            adultCount++
            binding.tvAdultCount.text = adultCount.toString()
            triggerPriceCalculation()
        }

        binding.btnMinusAdult.setOnClickListener {
            if (adultCount > 1) {
                adultCount--
                binding.tvAdultCount.text = adultCount.toString()
                triggerPriceCalculation()
            }
        }

        binding.btnPlusKid.setOnClickListener {
            kidCount++
            binding.tvKidCount.text = kidCount.toString()
            calculateLocalPrice()
            triggerPriceCalculation()
        }
        binding.btnMinusKids.setOnClickListener {
            if (kidCount > 1) {
                kidCount--
                binding.tvKidCount.text = kidCount.toString()
                calculateLocalPrice()
                triggerPriceCalculation()
            }
        }

        return binding.root
    }


    private fun saveActivityIdLocally(activityId: String) {
        val prefs = requireContext().getSharedPreferences("MyPrefs", 0)
        prefs.edit().putString("activityId", activityId).apply()

    }

    private fun calculateLocalPrice() {
        val adultPrice = price.toDoubleOrNull() ?: 0.0
        val childPrice = childrenPrice.toDoubleOrNull() ?: 0.0

        val subtotal = (adultCount * adultPrice) + (kidCount * childPrice)
        val total = subtotal

        binding.tvSubtotal.text = "R$subtotal"
        binding.tvTotal.text = "R$total"
    }

    private fun triggerPriceCalculation() {
        if (selectedDate.isNotEmpty()) {
            priceCalculateApi()
        }
    }

    private fun priceCalculateObserver() {
        priceCalculationViewModel.progressIndicator.observe(viewLifecycleOwner) {
        }

        priceCalculationViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            if (success == true) {
                val priceResponse = response.peekContent().data
                binding.tvPrice.text = "Adults - R${priceResponse?.prcingDetail?.adultCount}"
                binding.tvKids.text = "Kids - R${priceResponse?.prcingDetail?.kidsCount}"

                binding.tvAdultCount.text = priceResponse?.prcingDetail?.adult.toString()
                binding.tvKidCount.text = priceResponse?.prcingDetail?.kids.toString()
                binding.tvSubtotal.text = priceResponse?.prcingDetail?.subtotal
                binding.tvTotal.text = priceResponse?.prcingDetail?.total
            }
        }


        priceCalculationViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun priceCalculateApi() {
        val body = PriceCalculationBody(
            activity_id = activityId,
            date = selectedDate,
            adultcount = adultCount.toString(),
            kidscount = kidCount.toString()
        )
        priceCalculationViewModel.priceCalculationApi(progressDialog, requireActivity(), body)
    }

    private fun datePickerCode() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val monthFormatted = String.format("%02d", selectedMonth + 1)
                val dayFormatted = String.format("%02d", selectedDay)

                // yyyy-MM-dd format
                selectedDate = "$selectedYear-$monthFormatted-$dayFormatted"

                binding.tvDate.text = selectedDate
                triggerPriceCalculation()
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }
}