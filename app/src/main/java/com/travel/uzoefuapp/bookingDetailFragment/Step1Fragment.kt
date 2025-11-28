package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activityTimeModel.ActivityTimeBody
import com.travel.uzoefuapp.activityTimeModel.ActivityTimeResponse
import com.travel.uzoefuapp.activityTimeModel.ActivityTimeViewModel
import com.travel.uzoefuapp.adapter.TimeSlotAdapter
import com.travel.uzoefuapp.databinding.FragmentStep1Binding
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationBody
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    private var selectedTime: String? = null
    private var selectedDatePre: String? = null
    private var availableSeat: Int = 0
    private var activityTimeList: List<ActivityTimeResponse.Datum> = emptyList()
    private var shouldOpenPopup = false
    private var popupWindow: PopupWindow? = null

    companion object {
        fun newInstance(
            price: String,
            childrenPrice: String,
            activityId: String,
            address: String,
            town: String,
            productName: String,
            selectedTime: String,
            selectedDate: String,
        ): Step1Fragment {
            val fragment = Step1Fragment()
            val args = Bundle()
            args.putString("price", price)
            args.putString("childrenPrice", childrenPrice)
            args.putString("activityId", activityId)
            args.putString("address", address)
            args.putString("town", town)
            args.putString("productName", productName)
            args.putString("selectedTime", selectedTime)
            args.putString("selectedDate", selectedDate)
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
            selectedTime = it.getString("selectedTime")
            selectedDatePre = it.getString("selectedDate")
        }
        binding.datePicker.setOnClickListener { datePickerCode() }

        binding.tvTitle.text = address + town
        binding.tvSubtitle.text = productName

        if (selectedTime == "") {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            selectedDate = String.format("%04d-%02d-%02d", year, month, day)
            binding.tvDate.text = selectedDate
            // binding.tvSelectedTime.text = selectedTime
            binding.tvSelectedTime.text = "Select Time"

            if (selectedDate.isEmpty()) {
                val sharedPref =
                    requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("selected_time", selectedTime).apply()
            } else {
                val sharedPref =
                    requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("selected_time", selectedTime).apply()
            }
        } else {
            binding.tvSelectedTime.text = selectedTime
            binding.tvDate.text = selectedDatePre
            selectedDate = selectedDatePre.toString()
            Log.e("SelectDate", "onCreateViewBBBBBBBBBBBBBBB $selectedDate")
            val sharedPref =
                requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putString("selected_time", selectedTime).apply()
        }

        activityId?.let { saveActivityIdLocally(it) }
        // priceCalculateApi()
        priceCalculateObserver()
        priceCalculateApi()
        activityTimeApi(selectedDate)

        binding.selectTime.setOnClickListener {
            shouldOpenPopup = true
            showTimeSlotPopup(it, binding.tvSelectedTime, selectedDate)
        }

        binding.btnPlusAdult.setOnClickListener {
            if (adultCount + kidCount < availableSeat) {
                adultCount++
                binding.tvAdultCount.text = adultCount.toString()
                triggerPriceCalculation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Only $availableSeat seats available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnMinusAdult.setOnClickListener {
            if (adultCount > 1) {
                adultCount--
                binding.tvAdultCount.text = adultCount.toString()
                triggerPriceCalculation()
            }
        }

        binding.btnPlusKid.setOnClickListener {
            if (adultCount + kidCount < availableSeat) {
                kidCount++
                binding.tvKidCount.text = kidCount.toString()
                calculateLocalPrice()
                triggerPriceCalculation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Only $availableSeat seats available",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    private fun showTimeSlotPopup(
        anchorView: View,
        tvSelectedTime: TextView,
        selectedDate: String
    ) {

        if (selectedDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a date first", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // ----- IMPORTANT: If popup already open → close first -----
        popupWindow?.dismiss()

        activityTimeViewModel.activityTimeResponse.observe(viewLifecycleOwner) { response ->

            val success = response.peekContent().success
            val data = response.peekContent().data

            if (success == true && !data.isNullOrEmpty()) {

                activityTimeList = data
                val timeSlots = data.map { it.time ?: "Unknown" }

                val popupView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.popup_time_slots, null)

                popupWindow = PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                ).apply {
                    setBackgroundDrawable(ColorDrawable(Color.WHITE))
                    isOutsideTouchable = true   // Allow outside touch dismiss
                    isFocusable = true
                    elevation = 20f              // Smooth shadow
                }

                val rvTimeSlotsPopup =
                    popupView.findViewById<RecyclerView>(R.id.rvTimeSlotsPopup)

                val sharedPref =
                    requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)

                val adapter = TimeSlotAdapter(timeSlots) { selectedTime ->

                    tvSelectedTime.text = selectedTime
                    sharedPref.edit().putString("selected_time", selectedTime).apply()

                    val index = timeSlots.indexOf(selectedTime)
                    if (index != -1) {
                        availableSeat = activityTimeList[index].available ?: 0
                    }

                    adultCount = 1
                    kidCount = 0

                    binding.tvAdultCount.text = adultCount.toString()
                    binding.tvKidCount.text = kidCount.toString()

                    triggerPriceCalculation()

                    popupWindow?.dismiss()   // GUARANTEED dismiss
                }

                rvTimeSlotsPopup.layoutManager = GridLayoutManager(requireContext(), 4)
                rvTimeSlotsPopup.adapter = adapter

                popupWindow?.showAsDropDown(anchorView)

            } else {
                Toast.makeText(requireContext(), "No time slots found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*    private fun showTimeSlotPopup(
            anchorView: View,
            tvSelectedTime: TextView,
            selectedDate: String
        ) {
            if (selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date first", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            activityTimeViewModel.activityTimeResponse.observe(viewLifecycleOwner) { response ->

                val success = response.peekContent().success
                val data = response.peekContent().data

                if (success == true && !data.isNullOrEmpty()) {

                    // Save full list
                    activityTimeList = data

                    val timeSlots: List<String> = data.map { it.time ?: "Unknown" }

                    val popupView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.popup_time_slots, null)

                    val popupWindow = PopupWindow(
                        popupView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true
                    ).apply {
                        setBackgroundDrawable(ColorDrawable(Color.WHITE))
                        isOutsideTouchable = false
                        isFocusable = true
                    }

                    val rvTimeSlotsPopup = popupView.findViewById<RecyclerView>(R.id.rvTimeSlotsPopup)

                    val sharedPref =
                        requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)

                    val adapter = TimeSlotAdapter(timeSlots) { selectedTime ->

                        tvSelectedTime.text = selectedTime

                        // Save time
                        sharedPref.edit().putString("selected_time", selectedTime).apply()

                        // --- IMPORTANT ---
                        // Find index of selected time → get correct availableSeat
                        val index = timeSlots.indexOf(selectedTime)

                        if (index != -1) {
                            availableSeat = activityTimeList[index].available ?: 0
                        }

                        // Toast.makeText(requireContext(), "Seats Available: $availableSeat", Toast.LENGTH_SHORT).show()

                        // ⭐ RESET PASSENGER COUNTS WHEN TIME CHANGES
                        adultCount = 1     // or 0 — jo aap chaho
                        kidCount = 0

                        binding.tvAdultCount.text = adultCount.toString()
                        binding.tvKidCount.text = kidCount.toString()

                        triggerPriceCalculation()

                        popupWindow?.dismiss()
                    }

                    rvTimeSlotsPopup.layoutManager = GridLayoutManager(requireContext(), 4)
                    rvTimeSlotsPopup.adapter = adapter

                    popupWindow.showAsDropDown(anchorView)

                } else {
                    Toast.makeText(requireContext(), "No time slots found", Toast.LENGTH_SHORT).show()
                }
            }

            activityTimeViewModel.errorResponse.observe(viewLifecycleOwner) {
                ErrorUtil.handlerGeneralError(requireActivity(), it)
            }
        }*/

    /*    private fun showTimeSlotPopup(
            anchorView: View,
            tvSelectedTime: TextView,
            selectedDate: String
        ) {
            if (selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date first", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            val dayName = getDayNameFromDate(selectedDate)

            activityTimeViewModel.activityTimeResponse.observe(viewLifecycleOwner) { response ->
                val success = response.peekContent().success
                val data = response.peekContent().data

                if (success == true && !data.isNullOrEmpty()) {
                    val dayData = data.find { it.time.equals(dayName, ignoreCase = true) }
                    val timeSlots = dayData?.time ?: emptyList()


                    if (timeSlots.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "No time slots available for $dayName",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@observe
                    }

                    val popupView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.popup_time_slots, null)

                    val popupWindow = PopupWindow(
                        popupView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true
                    ).apply {
                        setBackgroundDrawable(ColorDrawable(Color.WHITE))
                        isOutsideTouchable = true
                        isFocusable = true
                    }

                    val rvTimeSlotsPopup = popupView.findViewById<RecyclerView>(R.id.rvTimeSlotsPopup)
                    *//*val adapter = TimeSlotAdapter(timeSlots) { selectedTime ->
                    tvSelectedTime.text = selectedTime
                    val sharedPref = requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("selected_time", selectedTime).apply()
                    popupWindow.dismiss()
                }*//*

                val sharedPref =
                    requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)

                // Agar TextView me "Select Time" hai, toh previous saved data remove karo
                if (tvSelectedTime.text.toString() == "Select Time") {
                    sharedPref.edit().remove("selected_time").apply()
                }

               // Fir Adapter set karo
                val adapter = TimeSlotAdapter(timeSlots) { selectedTime ->
                    tvSelectedTime.text = selectedTime
                    sharedPref.edit().putString("selected_time", selectedTime).apply()
                    popupWindow.dismiss()
                }

                //  rvTimeSlotsPopup.adapter = adapter


                rvTimeSlotsPopup.layoutManager = GridLayoutManager(requireContext(), 4)
                rvTimeSlotsPopup.adapter = adapter

                popupWindow.showAsDropDown(anchorView, 0, 0)
            } else {
                Toast.makeText(requireContext(), "No time slots found", Toast.LENGTH_SHORT).show()
            }
        }

        activityTimeViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }*/

    private fun getDayNameFromDate(dateString: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
    }

    /*
        private fun showTimeSlotPopup(anchorView: View, tvSelectedTime: TextView) {
            activityTimeViewModel.activityTimeResponse.observe(viewLifecycleOwner) { response ->
                val success = response.peekContent().success
                val data = response.peekContent().data

                if (success == true && !data.isNullOrEmpty()) {
                    val allTimeSlots = data.flatMap { it.availableTimes ?: emptyList() }.distinct()

                    if (allTimeSlots.isEmpty()) {
                        Toast.makeText(requireContext(), "No time slots available", Toast.LENGTH_SHORT)
                            .show()
                        return@observe
                    }

                    val popupView = LayoutInflater.from(requireContext())
                        .inflate(R.layout.popup_time_slots, null)

                    val popupWindow = PopupWindow(
                        popupView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true
                    ).apply {
                        setBackgroundDrawable(ColorDrawable(Color.WHITE))
                        isOutsideTouchable = true
                        isFocusable = true
                    }

                    val rvTimeSlotsPopup = popupView.findViewById<RecyclerView>(R.id.rvTimeSlotsPopup)
                    val adapter = TimeSlotAdapter(allTimeSlots) { selectedTime ->
                        // Set selected time to TextView
                        tvSelectedTime.text = selectedTime

                        // Save selected time in SharedPreferences
                        val sharedPref =
                            requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("selected_time", selectedTime).apply()

                        popupWindow.dismiss()
                    }

                    rvTimeSlotsPopup.layoutManager = GridLayoutManager(requireContext(), 4)
                    rvTimeSlotsPopup.adapter = adapter

                    popupWindow.showAsDropDown(anchorView, 0, 0)

                } else {
                    Toast.makeText(requireContext(), "No time slots found", Toast.LENGTH_SHORT).show()
                }
            }

            activityTimeViewModel.errorResponse.observe(viewLifecycleOwner) {
                ErrorUtil.handlerGeneralError(requireActivity(), it)
            }
        }
    */

    private fun activityTimeApi(selectedDay: String) {
        val body = ActivityTimeBody(
            activityId = activityId.toString(),
            date = selectedDay.toString()
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

        binding.tvSubtotal.text = "R $subtotal"
        binding.tvTotal.text = "R $total"
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

    /*
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

                    selectedDate = "$selectedYear-$monthFormatted-$dayFormatted"

                    binding.tvDate.text = selectedDate
                    triggerPriceCalculation()
                    activityTimeApi(selectedDay)
                },
                year, month, day
            )
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    */

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

                selectedDate = "$selectedYear-$monthFormatted-$dayFormatted"

                binding.tvDate.text = selectedDate

                triggerPriceCalculation()

                // 🚀 Always send FULL date in string format
                activityTimeApi(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    override fun onResume() {
        super.onResume()
        val body = ActivityTimeBody(
            activityId = activityId.toString(),
            date = selectedDate
        )
        activityTimeViewModel.activityTimeApi(requireActivity(), progressDialog, body)

        shouldOpenPopup = false  // prevent auto popup opening

        restoreSelectedTimeAndSeat()
    }

    private fun restoreSelectedTimeAndSeat() {
        val sharedPref =
            requireContext().getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
        val savedTime = sharedPref.getString("selected_time", "") ?: ""

        if (savedTime.isNotEmpty()) {
            binding.tvSelectedTime.text = savedTime
        }

        // Wait for API data
        activityTimeViewModel.activityTimeResponse.observe(viewLifecycleOwner) { response ->

            val data = response.peekContent().data ?: return@observe

            activityTimeList = data
            val timeSlots = data.map { it.time ?: "" }

            val index = timeSlots.indexOf(savedTime)

            if (index != -1) {
                availableSeat = data[index].available ?: 0
            }

            // OPTIONAL: If you want to reset counts onResume
            adultCount = 1
            kidCount = 0

            binding.tvAdultCount.text = adultCount.toString()
            binding.tvKidCount.text = kidCount.toString()

            triggerPriceCalculation()
        }
    }


}