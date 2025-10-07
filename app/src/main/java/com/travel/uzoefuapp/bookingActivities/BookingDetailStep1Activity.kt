package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.travel.uzoefuapp.paymentModel.PaymentViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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

        // Get values from intent
        price = intent.getStringExtra("price").toString()
        childrenPrice = intent.getStringExtra("childrenPrice").toString()
        activityId = intent.getStringExtra("activityId").toString()
        town = intent.getStringExtra("town").toString()
        address = intent.getStringExtra("address").toString()
        productName = intent.getStringExtra("productName").toString()

        paymentObserver()

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
            Step1Fragment.newInstance(
                price,
                childrenPrice,
                activityId,
                address,
                town,
                productName
            )
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

        nextBtn.setOnClickListener {
            when {
                currentStep < 4 -> {
                    // Step 1, 2, 3
                    currentStep++
                    when (currentStep) {
                        2 -> openFragment(Step2Fragment())
                        3 -> openFragment(Step3Fragment(activityId))
                        4 -> openFragment(Step4Fragment.newInstance(activityId, productName))

                    }
                    updateStepper()
                }

                currentStep == 4 -> {
                    // ✅ Step4 pe Pay Now button click -> API call
                    callPaymentApi()
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

        Log.d(
            "PaymentLog",
            "Profile -> First: $firstName, Last: $lastName, Username: $username, Mobile: $mobile"
        )
        Log.d("PaymentLog", "Billing Address: $billingAddress")

        // 4️⃣ Null-safe extension
        fun String?.toSafeRequestBody(): RequestBody =
            (this ?: "").toRequestBody("text/plain".toMediaTypeOrNull())

        // 5️⃣ Get participants dynamically from SharedPreferences
        val participants = getParticipantsFromPrefs() // your existing function
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


        /*       val clientNamesParts = participants.mapIndexed { index, participant ->
                   MultipartBody.Part.createFormData("clientname[]", "client_$index", participant.clientName.toSafeRequestBody())
               }

               val idNumbersParts = participants.mapIndexed { index, participant ->
                   MultipartBody.Part.createFormData("idnumber[]", "id_$index", participant.idNumber.toSafeRequestBody())
               }

               val contactNumbersParts = participants.mapIndexed { index, participant ->
                   MultipartBody.Part.createFormData("contactnumber[]", "contact_$index", participant.contactNumber.toSafeRequestBody())
               }

               val signInDatesParts = participants.mapIndexed { index, participant ->
                   MultipartBody.Part.createFormData("signindate[]", "date_$index", participant.dateSigned.toSafeRequestBody())
               }*/

        // 3️⃣ Convert Base64 signatures to files and prepare multipart
        fun base64ToFile(base64String: String, fileName: String): File {
            val bytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
            val file = File(cacheDir, fileName)
            file.outputStream().use { it.write(bytes) }
            return file
        }

        val signatureFilesParts = participants.mapIndexedNotNull { index, p ->
            p.signatureBase64.let { base64 ->
                try {
                    val file = base64ToFile(base64, "signature_$index.png")
                    val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("signature[]", file.name, requestFile)
                } catch (e: Exception) {
                    Log.e("SignatureError", "Failed to create file from base64: ${e.message}")
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