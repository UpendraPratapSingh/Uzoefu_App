package com.travel.uzoefuapp.activities

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.changePasswordModel.ChangePasswordBody
import com.travel.uzoefuapp.changePasswordModel.ChangePasswordViewModel
import com.travel.uzoefuapp.databinding.ActivityChangePasswordBinding
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    private var isNewPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private val progressDialog by lazy { CustomProgressDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        changePasswordObserver()

        binding.arrowBack.setOnClickListener { finish() }

        binding.submitButton.setOnClickListener { formVelidation() }

        binding.oldPasswordImage.setOnClickListener {
            isNewPasswordVisible = !isNewPasswordVisible
            if (isNewPasswordVisible) {
                binding.oldPasswordTxt.transformationMethod = null
                binding.oldPasswordImage.setImageResource(R.drawable.passwordhide)
            } else {
                binding.oldPasswordTxt.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.oldPasswordImage.setImageResource(R.drawable.passwordshow)
            }
            binding.oldPasswordTxt.setSelection(binding.oldPasswordTxt.text?.length ?: 0)
        }

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

    private fun changePasswordObserver() {
        changePasswordViewModel.progressIndicator.observe(this) {

        }
        changePasswordViewModel.changePasswordResponse.observe(this) { response ->
            val success = response.peekContent().status
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ChangePasswordActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        changePasswordViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ChangePasswordActivity, it)
        }
    }

    private fun formVelidation() {
        val oldPassword = binding.oldPasswordTxt.text.toString().trim()
        val newPassword = binding.oldPassword.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (VelidationInputs(oldPassword, newPassword, confirmPassword)) {
            //calling the api of reset password
            resetPasswordApi(oldPassword, newPassword, confirmPassword)

        }
    }

    private fun resetPasswordApi(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        val body = ChangePasswordBody(
            oldPassword = oldPassword,
            newPassword = newPassword,
            confirmPassword = confirmPassword
        )
        changePasswordViewModel.changePasswordApi(this, progressDialog, body)

    }

    private fun VelidationInputs(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        return when {
            oldPassword.isEmpty() -> {
                binding.oldPasswordTxt.error = "Please Enter Old Password"
                false
            }

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