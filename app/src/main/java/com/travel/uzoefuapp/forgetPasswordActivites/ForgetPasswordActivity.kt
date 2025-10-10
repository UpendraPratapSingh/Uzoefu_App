package com.travel.uzoefuapp.forgetPasswordActivites

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityForgetPasswordBinding
import com.travel.uzoefuapp.forgetPasswordModel.ForgotPasswordBody
import com.travel.uzoefuapp.forgetPasswordModel.ForgotPasswordViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.arrowBack.setOnClickListener { finish() }

        forgotPasswordObserver()

        binding.forgotbutton.setOnClickListener { formVelidation() }
    }

    private fun formVelidation() {
        val email = binding.emailtext.text.toString().trim()

        if (VelidationInputs(email)) {
            //calling api here
            forgotPasswordApi(email)

        }
    }

    private fun forgotPasswordApi(email: String) {
        val body = ForgotPasswordBody(email = email)
        forgotPasswordViewModel.forgetPasswordApi(progressDialog, this, body)
    }

    private fun forgotPasswordObserver() {
        forgotPasswordViewModel.progressIndicator.observe(this) {

        }
        forgotPasswordViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val id = response.peekContent().data?.id.toString()

            if (success == true) {
                val intent =
                    Intent(this@ForgetPasswordActivity, OtpVerificationActivity::class.java)
                intent.putExtra("userId", id)
                startActivity(intent)
            }

        }
        forgotPasswordViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ForgetPasswordActivity, it)
        }
    }

    private fun VelidationInputs(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.emailtext.error = "Please enter your email"
                false
            }

            else -> true
        }
    }
}