package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingDetailFragment.Participant
import com.travel.uzoefuapp.bookingDetailFragment.Step1Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step2Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step3Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step4Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step5Fragment
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.ActivityBookingDetailStep1Binding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.paymentModel.PaymentViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class BookingDetailStep1Activity : AppCompatActivity() {
    lateinit var binding: ActivityBookingDetailStep1Binding
    private lateinit var steps: List<TextView>
    private lateinit var lines: List<View>
    private lateinit var nextBtn: Button
    private var currentStep = 1
    var price = ""
    private var childrenPrice = ""
    private var activityId = ""
    private var address = ""
    private var town = ""
    private var productName = ""
    private val paymentViewModel: PaymentViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingDetailStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        price = intent.getStringExtra("price").toString()
        childrenPrice = intent.getStringExtra("childrenPrice").toString()
        activityId = intent.getStringExtra("activityId").toString()
        town = intent.getStringExtra("town").toString()
        address = intent.getStringExtra("address").toString()
        productName = intent.getStringExtra("productName").toString()

        paymentObserver()
        notificationCountApi()
        notificationCountObserver()

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookingDetailStep1Activity, NotificationActivity::class.java)
            startActivity(intent)
        }

        steps = listOf(
            findViewById(R.id.step1),
            findViewById(R.id.step2),
            findViewById(R.id.step3),
            findViewById(R.id.step4),
            findViewById(R.id.step5)
        )

        lines = listOf(
            findViewById(R.id.line1),
            findViewById(R.id.line2),
            findViewById(R.id.line3),
            findViewById(R.id.line4)
        )

        nextBtn = findViewById(R.id.nextButton)

        binding.btnBack.setOnClickListener { finish() }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@BookingDetailStep1Activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        openFragment(
            Step1Fragment.newInstance(price, childrenPrice, activityId, address, town, productName)
        )
        updateStepper()

        /*
                nextBtn.setOnClickListener {
                    if (currentStep < 5) {
                        currentStep++
                        when (currentStep) {
                            2 -> openFragment(Step2Fragment())
                            3 -> openFragment(Step3Fragment(activityId))
                            4 -> openFragment(Step4Fragment.newInstance(activityId, productName))
                            5 -> openFragment(Step5Fragment.newInstance(activityId, productName))
                        }
                        updateStepper()
                    } else {
                        val intent = Intent(this@BookingDetailStep1Activity, DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
        */

        /*
                nextBtn.setOnClickListener {
                    when {
                        currentStep < 4 -> {
                            // Step 1, 2, 3
                            currentStep++
                            when (currentStep) {
                                2 -> {
                                    if (validateStep1Fields()) {
                                        currentStep = 2
                                        openFragment(Step2Fragment())
                                        updateStepper()
                                    }
                                }
                                3 -> openFragment(Step3Fragment(activityId))
                                4 -> openFragment(Step4Fragment.newInstance(activityId, productName))

                            }
                            updateStepper()
                        }

                        currentStep == 4 -> {
                            // ✅ Step4 pe Pay Now button click -> API call
                            callPaymentApi()
                            paymentObserver()
                        }

                        currentStep == 5 -> {
                            // ✅ Step5 confirmation pe Finish -> Dashboard
                            val intent =
                                Intent(this@BookingDetailStep1Activity, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
        */


        nextBtn.setOnClickListener {
            when (currentStep) {
                1 -> {
                    if (validateStep1Fields()) {
                        currentStep = 2
                        openFragment(Step2Fragment())
                        updateStepper()
                    }
                }

                2 -> {
                    if (validateStep2Fields()) {
                        currentStep = 3
                        openFragment(Step3Fragment(activityId))
                        updateStepper()
                    }
                }

                3 -> {
                    if (validateStep3Fields()) {
                        currentStep = 4
                        openFragment(Step4Fragment.newInstance(activityId, productName))
                        updateStepper()
                    }
                }

                4 -> {
                    callPaymentApi()
                    paymentObserver()
                }

                5 -> {
                    val intent =
                        Intent(this@BookingDetailStep1Activity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateStep3Fields(): Boolean {
        val participants = getParticipantsFromPrefs()

        if (participants.isEmpty()) {
            Toast.makeText(this, "Add at least one participant", Toast.LENGTH_SHORT).show()
            return false
        }
        participants.forEachIndexed { index, participant ->
            if (participant.clientName.isEmpty() ||
                participant.idNumber.isEmpty() ||
                participant.contactNumber.isEmpty() ||
                participant.dateSigned.isEmpty() ||
                participant.signatureBase64.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please fill all fields and add signature for participant ${index + 1}",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    private fun validateStep2Fields(): Boolean {
        val sharedPrefProfile = getSharedPreferences("profile_pref", Context.MODE_PRIVATE)
        val firstName = sharedPrefProfile.getString("first_name", "") ?: ""
        val lastName = sharedPrefProfile.getString("last_name", "") ?: ""
        val username = sharedPrefProfile.getString("username", "") ?: ""
        val mobile = sharedPrefProfile.getString("mobile", "") ?: ""
        val billingAddress = sharedPrefProfile.getString("billing_address", "") ?: ""

        return when {
            firstName.isEmpty() -> {
                Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show()
                false
            }

            lastName.isEmpty() -> {
                Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show()
                false
            }

            username.isEmpty() -> {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                false
            }

            mobile.isEmpty() -> {
                Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show()
                false
            }

            mobile.length < 10 -> {
                Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
                false
            }

            billingAddress.isEmpty() -> {
                Toast.makeText(this, "Please enter billing address", Toast.LENGTH_SHORT).show()
                false
            }

            else -> true
        }
    }


    private fun validateStep1Fields(): Boolean {
        // Get selected date from booking_pref
        val bookingPref = getSharedPreferences("booking_pref", Context.MODE_PRIVATE)
        val selectedDate = bookingPref.getString("date", "") ?: ""

        // Get selected time from ActivityPrefs
        val activityPref = getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
        val selectedTime = activityPref.getString("selected_time", "") ?: ""

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedTime.isNullOrEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show()
            return false
        }

        val adultCount = bookingPref.getInt("adultcount", 0)
        if (adultCount < 1) {
            Toast.makeText(this, "Please add at least one adult", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun callPaymentApi() {
        // 1️⃣ Toast for testing
        Toast.makeText(this, "Payment Done Successfully", Toast.LENGTH_SHORT).show()

        currentStep = 5
        openFragment(Step5Fragment.newInstance(activityId, productName))
        updateStepper()

        // 2️⃣ Booking info from SharedPreferences
        val sharedPref = getSharedPreferences("booking_pref", Context.MODE_PRIVATE)
        val date = sharedPref.getString("date", "") ?: ""
        val adultCount = sharedPref.getInt("adultcount", 0)
        val kidsCount = sharedPref.getInt("kidscount", 0)
        /*val adultPrice = sharedPref.getString("adultprice", "0.00") ?: "0.00"
        val kidsPrice = sharedPref.getString("kidsprice", "0.00") ?: "0.00"
        val subtotal = sharedPref.getString("subtotal", "0.00") ?: "0.00"
        val total = sharedPref.getString("total", "0.00") ?: "0.00"*/
        val activityId = sharedPref.getString("activity_id", "") ?: ""

        val adultPrice = sharedPref.getInt("adultprice", 0)
        val kidsPrice = sharedPref.getInt("kidsprice", 0)
        val subtotal = sharedPref.getInt("subtotal", 0)
        val total = sharedPref.getInt("total", 0)



        Log.d("PaymentLog", "SharedPrefs -> Date: $date, Adults: $adultCount, Kids: $kidsCount")
        Log.d(
            "PaymentLog",
            "Prices -> Adult: $adultPrice, Kids: $kidsPrice, Subtotal: $subtotal, Total: $total"
        )
        Log.d("PaymentLog", "ActivityId: $activityId")

        // 3️⃣ Profile info dynamic
        val sharedPrefProfile = getSharedPreferences("profile_pref", Context.MODE_PRIVATE)
        val firstName = sharedPrefProfile.getString("first_name", "") ?: ""
        val lastName = sharedPrefProfile.getString("last_name", "") ?: ""
        val username = sharedPrefProfile.getString("username", "") ?: ""
        val mobile = sharedPrefProfile.getString("mobile", "") ?: ""
        val billingAddress = sharedPrefProfile.getString("billing_address", "") ?: ""

        val sharedPrefTime = getSharedPreferences("ActivityPrefs", Context.MODE_PRIVATE)
        val selectedTime = sharedPrefTime.getString("selected_time", "")


        // 4️⃣ Null-safe extension
        fun String?.toSafeRequestBody(): RequestBody =
            (this ?: "").toRequestBody("text/plain".toMediaTypeOrNull())

        // 5️⃣ Get participants dynamically from SharedPreferences
        val participants = getParticipantsFromPrefs()
        if (participants.isEmpty()) {
            Toast.makeText(this, "Add at least one participant", Toast.LENGTH_SHORT).show()
            return
        }

        val clientNamesParts = participants.map { participant ->
            MultipartBody.Part.createFormData("clientname[]", participant.clientName)
        }

        val idNumbersParts = participants.map { participant ->
            MultipartBody.Part.createFormData("idnumber[]", participant.idNumber)
        }

        val contactNumbersParts = participants.map { participant ->
            MultipartBody.Part.createFormData("contactnumber[]", participant.contactNumber)
        }

        val signInDatesParts = participants.map { participant ->
            MultipartBody.Part.createFormData("signindate[]", participant.dateSigned)
        }

        fun base64ToFile(base64String: String, fileName: String): File {
            val cleanBase64 = base64String.substringAfter(",")
            val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true)

            val file = File(cacheDir, fileName)
            FileOutputStream(file).use { fos ->
                resized.compress(Bitmap.CompressFormat.JPEG, 60, fos)
            }

            Log.d(
                "SignatureFile",
                "File path: ${file.absolutePath}, Size: ${file.length() / 1024} KB"
            )
            return file
        }


        val signatureFilesParts = participants.mapIndexedNotNull { index, participant ->
            participant.signatureBase64.takeIf { it.isNotBlank() }?.let { base64 ->
                try {
                    // Build actual file name based on participant name or index
                    val safeName = participant.clientName
                        ?.replace("\\s+".toRegex(), "_")  // Replace spaces with underscores
                        ?.replace("[^A-Za-z0-9_]".toRegex(), "")  // Remove invalid characters
                        ?: "participant_$index"

                    val fileName = "${safeName}_signature_${System.currentTimeMillis()}.png"

                    // Convert base64 to file with actual name
                    val file = base64ToFile(base64, fileName)

                    if (file.exists() && file.length() > 0) {
                        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                        Log.d(
                            "SignatureFile",
                            "✅ Created file: ${file.name}, Size: ${file.length() / 1024} KB, Path: ${file.absolutePath}"
                        )
                        MultipartBody.Part.createFormData("signature[]", file.name, requestFile)
                    } else {
                        Log.e(
                            "SignatureError",
                            "⚠️ File not created or empty for participant: ${participant.clientName}"
                        )
                        null
                    }
                } catch (e: Exception) {
                    Log.e(
                        "SignatureError",
                        "❌ Failed to create file for ${participant.clientName}: ${e.message}"
                    )
                    null
                }
            }
        }

        // ✅ Ensure at least one signature
        if (signatureFilesParts.isEmpty()) {
            Toast.makeText(this, "Please add at least one signature", Toast.LENGTH_SHORT).show()
            return
        }

        // Log all parts
        Log.d(
            "PaymentLog",
            "ClientNames: ${clientNamesParts.size}, IDNumbers: ${idNumbersParts.size}, ContactNumbers: ${contactNumbersParts.size}, SignInDates: ${signInDatesParts.size}, Signatures: ${signatureFilesParts.size}"
        )

        //  Call ViewModel API
        paymentViewModel.ratingApi(
            progressDialog,
            activityId = activityId,
            date = date,
            times = selectedTime.toString(),
            adultCount = adultCount.toString(),
            kidsCount = kidsCount.toString(),
            adultPrice = adultPrice.toString(),
            kidsPrice = kidsPrice.toString(),
            subtotal = subtotal.toString(),
            total = total.toString(),
            firstName = firstName,
            surname = lastName,
            username = username,
            mobileNumber = mobile,
            billingAddress = billingAddress,
            clientNames = clientNamesParts,
            idNumbers = idNumbersParts,
            contactNumbers = contactNumbersParts,
            signInDates = signInDatesParts,
            signatureImages = signatureFilesParts
        )
    }

    private fun paymentObserver() {
        paymentViewModel.progressIndicator.observe(this) {}
        paymentViewModel.paymentResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            if (success == true) {
                Toast.makeText(this, message ?: "Booking successful", Toast.LENGTH_SHORT).show()
                currentStep = 5
                openFragment(Step5Fragment.newInstance(activityId, productName))
                updateStepper()
            } else {
                Toast.makeText(this, message ?: "Booking failed", Toast.LENGTH_SHORT).show()
            }
        }
        paymentViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun getParticipantsFromPrefs(): MutableList<Participant> {
        val sharedPref = this.getSharedPreferences("participants_pref", 0)
        val gson = Gson()
        val json = sharedPref.getString("participants_list", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Participant>>() {}.type
            gson.fromJson(json, type)
        } else mutableListOf()
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.tripFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    /*
        private fun updateStepper() {
            for (i in steps.indices) {
                if (i < currentStep) {
                    steps[i].setBackgroundResource(R.drawable.circle_active)
                    steps[i].setTextColor(resources.getColor(R.color.green_color, theme))
                } else {
                    steps[i].setBackgroundResource(R.drawable.circle_inactive)
                    steps[i].setTextColor(resources.getColor(android.R.color.darker_gray, theme))
                }
            }

            for (i in lines.indices) {
                if (i < currentStep) {
                    lines[i].setBackgroundResource(R.color.line_active)
                } else {
                    lines[i].setBackgroundResource(R.color.line_inactive)
                }
            }
        }
    */

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                binding.notificationBadge.text = data.toString()
            }

        }
        notificationCountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@BookingDetailStep1Activity, it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(this, progressDialog)

    }


    private fun updateStepper() {
        for (i in steps.indices) {
            if (i < currentStep) {
                steps[i].setBackgroundResource(R.drawable.circle_active)
                steps[i].setTextColor(resources.getColor(R.color.green_color, theme))
            } else {
                steps[i].setBackgroundResource(R.drawable.circle_inactive)
                steps[i].setTextColor(resources.getColor(android.R.color.darker_gray, theme))
            }
        }

        for (i in lines.indices) {
            if (i < currentStep) {
                lines[i].setBackgroundResource(R.color.line_active)
            } else {
                lines[i].setBackgroundResource(R.color.line_inactive)
            }
        }

        // ✅ Step4 -> Pay Now, Step5 -> Finish
        nextBtn.text = when (currentStep) {
            4 -> "Pay Now"
            5 -> "Confirm"
            else -> "Next"
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (currentStep > 1) {
            currentStep--
            when (currentStep) {
                1 -> openFragment(
                    Step1Fragment.newInstance(
                        price,
                        childrenPrice,
                        activityId,
                        address,
                        town,
                        productName
                    )
                )

                2 -> openFragment(Step2Fragment())
                3 -> openFragment(Step3Fragment(activityId))
                4 -> openFragment(Step4Fragment())
            }
            updateStepper()
        } else {
            finish()
        }
    }
}