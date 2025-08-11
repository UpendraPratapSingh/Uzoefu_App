package com.travel.uzoefuapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.travel.uzoefuapp.R
import android.text.Editable
import android.view.View
import com.travel.uzoefuapp.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playBackgroundVideo()

        binding.signInButton.setOnClickListener {
            val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this@CreateAccountActivity, LoginActivity::class.java)
            startActivity(intent)
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

                updateRuleIcon(binding.iconNoSpaces, password.isNotEmpty() && !password.contains(" "))

                updateRuleIcon(binding.iconNoEmailParts, password.isNotEmpty() && !emailPartFound(password, email))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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
            if (isValid) R.drawable.checkcircle else R.drawable.uncheckcircle
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
