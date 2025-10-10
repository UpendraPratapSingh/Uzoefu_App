package com.travel.uzoefuapp.forgetPasswordActivites

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.LoginActivity
import com.travel.uzoefuapp.databinding.ActivityResetPasswordBinding
import com.travel.uzoefuapp.forgetPasswordModel.ResetPasswordBody
import com.travel.uzoefuapp.forgetPasswordModel.ResetPasswordViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()
    private var userId = ""
    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.arrowBack.setOnClickListener { finish() }

        userId = intent.getStringExtra("userId").toString()

        Log.e("OtpVerification", "Received UserId: $userId")

        binding.submitButton.setOnClickListener {
            formVelidation()
        }
        resetPasswordObserver()

        binding.emailIcon.setOnClickListener {
            isNewPasswordVisible = !isNewPasswordVisible
            if (isNewPasswordVisible) {
                binding.oldPassword.transformationMethod = null
                binding.emailIcon.setImageResource(R.drawable.passwordhide)
            } else {
                binding.oldPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.emailIcon.setImageResource(R.drawable.passwordshow)
            }
            binding.oldPassword.setSelection(binding.oldPassword.text?.length ?: 0)
        }

        binding.confirmIcon.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                binding.confirmPassword.transformationMethod = null
                binding.confirmIcon.setImageResource(R.drawable.passwordhide)
            } else {
                binding.confirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.confirmIcon.setImageResource(R.drawable.passwordshow)
            }
            binding.confirmPassword.setSelection(binding.confirmPassword.text?.length ?: 0)
        }
    }

    private fun resetPasswordObserver() {
        resetPasswordViewModel.mCategoryResponse.observe(this) {

        }
        resetPasswordViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }
        resetPasswordViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ResetPasswordActivity, it)
        }
    }

    private fun formVelidation() {
        val newPassword = binding.oldPassword.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (VelidationInputs(newPassword, confirmPassword)) {
            //calling the api of reset password
            resetPasswordApi(newPassword, confirmPassword)

        }
    }

    private fun resetPasswordApi(newPassword: String, confirmPassword: String) {
        val body = ResetPasswordBody(
            password = newPassword,
            password_confirmation = confirmPassword,
            id = userId.toString()
        )
        resetPasswordViewModel.resetPasswordApi(progressDialog, this, body)

    }

    private fun VelidationInputs(newPassword: String, confirmPassword: String): Boolean {
        return when {
            newPassword.isEmpty() -> {
                binding.oldPassword.error = "Please New Password"
                false
            }

            confirmPassword.isEmpty() -> {
                binding.confirmPassword.error = "Please Enter Confirm Password"
                false
            }

            newPassword != confirmPassword -> {
                binding.confirmPassword.error = "Passwords do not match"
                false
            }

            else -> true
        }
    }
}