package com.travel.uzoefuapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityCreateBusinessAccountBinding

class CreateBusinessAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBusinessAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityCreateBusinessAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        playBackgroundVideo()

        binding.signInButton.setOnClickListener {
            val intent = Intent(this@CreateBusinessAccountActivity, LoginActivity::class.java)
            intent.putExtra("USER_TYPE", "Business")

            startActivity(intent)
        }

        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this@CreateBusinessAccountActivity, LoginActivity::class.java)
            intent.putExtra("USER_TYPE", "Business")

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

    @Suppress("DEPRECATION")
    private fun makeFullScreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
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

    private fun playBackgroundVideo() {
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.onboard1}")

        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }
}