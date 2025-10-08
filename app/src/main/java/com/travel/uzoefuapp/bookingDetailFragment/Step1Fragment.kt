package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.activityTimeModel.ActivityTimeBody
import com.travel.uzoefuapp.activityTimeModel.ActivityTimeViewModel
import com.travel.uzoefuapp.databinding.FragmentStep1Binding
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationBody
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class Step1Fragment() :
    Fragment() {
    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!
    private val priceCalculationViewModel: PriceCalculationViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private val activityTimeViewModel: ActivityTimeViewModel by viewModels()
    private var adultCount = 1
    private var kidCount = 1
    private var selectedDate = ""

    private var price: String? = null
    private var childrenPrice: String? = null
    private var activityId: String? = null
    private var address: String? = null
    private var town: String? = null
    private var productName: String? = null

    companion object {
        fun newInstance(
            price: String,
            childrenPrice: String,
            activityId: String,
            address: String,
            town: String,
            productName: String
        ): Step1Fragment {
            val fragment = Step1Fragment()
            val args = Bundle()
            args.putString("price", price)
            args.putString("childrenPrice", childrenPrice)
            args.putString("activityId", activityId)
            args.putString("address", address)
            args.putString("town", town)
            args.putString("productName", productName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStep1Binding.inflate(inflater, container, false)
        arguments?.let {
            price = it.getString("price")
            childrenPrice = it.getString("childrenPrice")
            activityId = it.getString("activityId")
            address = it.getString("address")
            town = it.getString("town")
            productName = it.getString("productName")
        }
        binding.datePicker.setOnClickListener { datePickerCode() }

        binding.tvTitle.text = address + town
        binding.tvSubtitle.text = productName

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        selectedDate = String.format("%04d-%02d-%02d", year, month, day)
        binding.tvDate.text = selectedDate
        activityId?.let { saveActivityIdLocally(it) }
        // priceCalculateApi()
        priceCalculateObserver()
        priceCalculateApi()
        activityTimeApi()
        activityTimeObserver()

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
            if (kidCount > 0) {
                kidCount--
                binding.tvKidCount.text = kidCount.toString()
                calculateLocalPrice()
                triggerPriceCalculation()
            }
        }

        return binding.root
    }

    private fun activityTimeObserver() {
        activityTimeViewModel.progressIndicator.observe(viewLifecycleOwner) {
            // handle loader if needed
        }

        activityTimeViewModel.activityTimeResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data

            if (success == true && data != null) {

                // ✅ Display with day name, but store only time
                val timeSlots = listOf(
                    "Mon: ${data.monFrom} - ${data.monTo}",
                    "Tue: ${data.tueFrom} - ${data.tueTo}",
                    "Wed: ${data.wedFrom} - ${data.wedTo}",
                    "Thu: ${data.thuFrom} - ${data.thuTo}",
                    "Fri: ${data.friFrom} - ${data.friTo}",
                    "Sat: ${data.satFrom} - ${data.satTo}",
                    "Sun: ${data.sunFrom} - ${data.sunTo}"
                )

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    timeSlots
                )
                binding.spinnerSelectTime.adapter = adapter

                binding.spinnerSelectTime.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedFullText = timeSlots[position]
                            // ✅ Extract only time range part after colon
                            val selectedTime = selectedFullText.substringAfter(":").trim()

                            // ✅ Save only "09:00 - 10:00" in SharedPreferences
                            val sharedPref = requireContext()
                                .getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
                            sharedPref.edit()
                                .putString("selected_time", selectedTime)
                                .apply()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            } else {
                Toast.makeText(requireContext(), message ?: "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        activityTimeViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun activityTimeApi() {
        val body = ActivityTimeBody(
            activityId = activityId.toString()
        )
        activityTimeViewModel.activityTimeApi(requireActivity(), progressDialog, body)

    }


    private fun saveActivityIdLocally(activityId: String) {
        val prefs = requireContext().getSharedPreferences("MyPrefs", 0)
        prefs.edit().putString("activityId", activityId).apply()

    }

    private fun calculateLocalPrice() {
        val adultPrice = price?.toDoubleOrNull() ?: 0.0
        val childPrice = childrenPrice?.toDoubleOrNull() ?: 0.0

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
        val sharedPref = requireContext().getSharedPreferences("booking_pref", Context.MODE_PRIVATE)

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
                binding.tvSubtotal.text = "R${priceResponse?.prcingDetail?.subtotal}"
                binding.tvTotal.text = "R${priceResponse?.prcingDetail?.total}"

                // Convert prices to integers before storing
                val adultPriceInt =
                    (priceResponse?.prcingDetail?.adultCount ?: "0.00").toDouble().toInt()
                val kidsPriceInt =
                    (priceResponse?.prcingDetail?.kidsCount ?: "0.00").toDouble().toInt()
                val subtotalInt =
                    (priceResponse?.prcingDetail?.subtotal ?: "0.00").toDouble().toInt()
                val totalInt = (priceResponse?.prcingDetail?.total ?: "0.00").toDouble().toInt()

                // Store in SharedPreferences
                with(sharedPref.edit()) {
                    putString("activity_id", activityId)
                    putString("date", priceResponse?.prcingDetail?.date ?: "")
                    putInt("adultcount", priceResponse?.prcingDetail?.adult ?: 0)
                    putInt("kidscount", priceResponse?.prcingDetail?.kids ?: 0)
                    putInt("adultprice", adultPriceInt)
                    putInt("kidsprice", kidsPriceInt)
                    putInt("subtotal", subtotalInt)
                    putInt("total", totalInt)
                    apply()
                }
            }
        }

        priceCalculationViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun priceCalculateApi() {
        val body = activityId?.let {
            PriceCalculationBody(
                activity_id = it,
                date = selectedDate,
                adultcount = adultCount.toString(),
                kidscount = kidCount.toString()
            )
        }
        if (body != null) {
            priceCalculationViewModel.priceCalculationApi(
                progressDialog,
                requireActivity(),
                body
            )
        }
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