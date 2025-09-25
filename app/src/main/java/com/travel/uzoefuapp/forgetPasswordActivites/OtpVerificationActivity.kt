package com.travel.uzoefuapp.forgetPasswordActivites

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityOtpVerificationBinding
import com.travel.uzoefuapp.forgetPasswordModel.OtpVerificationBody
import com.travel.uzoefuapp.forgetPasswordModel.OtpVerificationViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private val otpVerificationViewModel: OtpVerificationViewModel by viewModels()
    private var userId = ""
    private val progressDialog by lazy { CustomProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userId = intent.getStringExtra("userId").toString()

        Log.e("TAG", "onCreate: $userId")

        binding.arrowBack.setOnClickListener { finish() }

        binding.verifyBtn.setOnClickListener { formValidation() }

        otpVerificationObserver()

    }

    private fun otpVerificationObserver() {
        otpVerificationViewModel.progressIndicator.observe(this) {

        }
        otpVerificationViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                val intent = Intent(this@OtpVerificationActivity, ResetPasswordActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        }
        otpVerificationViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@OtpVerificationActivity, it)
        }
    }

    private fun formValidation() {
        val otp = binding.pinview.text.toString().trim()

        if (validationInputs(otp)) {
            // calling api here
            forgotPasswordApi(otp)
        }
    }

    private fun forgotPasswordApi(otp: String) {
        val body = OtpVerificationBody(otp = otp)
        otpVerificationViewModel.OtpVerificationApi(progressDialog, this, body)
    }

    private fun validationInputs(otp: String): Boolean {
        return when {
            otp.isEmpty() -> {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
                false
            }

            otp.length < 6 -> {
                Toast.makeText(this, "Enter full 6-digit OTP", Toast.LENGTH_SHORT).show()
                false
            }

            else -> true
        }
    }
}