package com.travel.uzoefuapp.activities

import CustomProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.ActivityLoginBinding
import com.travel.uzoefuapp.forgetPasswordActivites.ForgetPasswordActivity
import com.travel.uzoefuapp.loginModel.LoginBody
import com.travel.uzoefuapp.loginModel.LoginViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var phoneNumber: String = ""
    private var password: String = ""
    private var isPasswordVisible = false

    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        playBackgroundVideo()

        binding.signInButton.setOnClickListener {
            validation()
        }

        binding.forgotText.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.emailIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.passwordEdit.transformationMethod = null
                binding.emailIcon.setImageResource(R.drawable.lock)
            } else {
                binding.passwordEdit.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.emailIcon.setImageResource(R.drawable.lock)
            }
            binding.passwordEdit.setSelection(binding.passwordEdit.text?.length ?: 0)
        }
        loginObserver()

    }

    private fun loginObserver() {
        loginViewModel.progressIndicator.observe(this) {

        }
        loginViewModel.mRegisterResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val response = response.peekContent().data

            if (success == true) {
                Uzoefu.encryptedPrefs.userId = response?.userId.toString()
                Uzoefu.encryptedPrefs.isFirstTime = false
                Uzoefu.encryptedPrefs.bearerToken = "Bearer ${response?.token ?: ""}"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        loginViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@LoginActivity, it)
        }
    }

    private fun validation() {
        phoneNumber = binding.emailEdit.text.toString().trim()
        password = binding.passwordEdit.text.toString().trim()

        when {
            phoneNumber.isEmpty() -> {
                Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()
            }

            password.isEmpty() -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                loginApi(phoneNumber, password)
            }
        }
    }

    private fun loginApi(phoneNo: String, passwordTxt: String) {
        val body = LoginBody(email = phoneNo, password = passwordTxt)
        loginViewModel.userLoginApi(progressDialog, this, body)
    }

    @Suppress("DEPRECATION")
    private fun makeFullScreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun playBackgroundVideo() {
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.onboard1}")

        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }
}