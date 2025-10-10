package com.travel.uzoefuapp.activities

import CustomProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.travel.uzoefuapp.R
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.databinding.ActivityCreateAccountBinding
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val signUpViewModel: SignUpViewModel by viewModels()
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        playBackgroundVideo()

        //called observer
        signUpObserver()

        binding.signInButton.setOnClickListener { getSignUp() }

        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.passwordEdit.transformationMethod = null
                binding.passwordToggle.setImageResource(R.drawable.passwordhide)
            } else {
                binding.passwordEdit.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.passwordToggle.setImageResource(R.drawable.passwordshow)
            }
            binding.passwordEdit.setSelection(binding.passwordEdit.text?.length ?: 0)
        }
        binding.passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s?.toString() ?: ""
                val email = "user@example.com"

                updateRuleIcon(binding.iconLength, password.length in 8..30)

                val hasNumber = password.any { it.isDigit() }
                val hasSpecial = password.any { "!@#\$%^&*()_+-=[]|;:'\",.<>?/".contains(it) }
                updateRuleIcon(binding.iconNumberSpecial, hasNumber && hasSpecial)

                val hasUpper = password.any { it.isUpperCase() }
                val hasLower = password.any { it.isLowerCase() }
                updateRuleIcon(binding.iconUpperLower, hasUpper && hasLower)
                updateRuleIcon(
                    binding.iconNoSpaces,
                    password.isNotEmpty() && !password.contains(" ")
                )
                updateRuleIcon(
                    binding.iconNoEmailParts,
                    password.isNotEmpty() && !emailPartFound(password, email)
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getSignUp() {
        if (binding.userNameEdit.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your First Name", Toast.LENGTH_LONG)
                .show()
        } else if (binding.userNameLastEdit.text.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "Please enter your Last Name", Toast.LENGTH_LONG)
                .show()
        } else if (binding.emailEdit.text.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "Enter Your email address", Toast.LENGTH_LONG).show()

        } else if (binding.passwordEdit.text.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "enter your Password", Toast.LENGTH_LONG).show()

        } else {
            signUpApi()
        }
    }

    private fun signUpApi() {
        val signUpBody = SignUpBody(
            contactName = binding.userNameEdit.text.toString(),
            lastName = binding.userNameLastEdit.text.toString(),
            email = binding.emailEdit.text.toString(),
            password = binding.passwordEdit.text.toString(),

            )
        signUpViewModel.signUpUser(progressDialog, this, signUpBody)
    }

    private fun signUpObserver() {
        signUpViewModel.progressIndicator.observe(this) {}
        signUpViewModel.mRegisterResponse.observe(this) {
            val message = it.peekContent().message!!
            val success = it.peekContent().success!!

            if (success) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

            }
        }

        signUpViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    @Suppress("DEPRECATION")
    private fun makeFullScreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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

    fun updateRuleIcon(imageView: ImageView, isValid: Boolean) {
        imageView.setImageResource(
            if (isValid) R.drawable.checkcircle else R.drawable.circlecheckbox
        )
    }

    fun emailPartFound(password: String, email: String): Boolean {
        val username = email.substringBefore("@").lowercase()
        val lowerPass = password.lowercase()

        for (i in 0..username.length - 3) {
            val part = username.substring(i, i + 3)
            if (lowerPass.contains(part)) {
                return true
            }
        }
        return false
    }
}
