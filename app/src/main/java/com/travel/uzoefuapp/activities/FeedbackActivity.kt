package com.travel.uzoefuapp.activities

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityFeedbackBinding
import com.travel.uzoefuapp.feedback.FeedbackBody
import com.travel.uzoefuapp.feedback.FeedbackViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val feedbackViewModel: FeedbackViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener { finish() }

        feedbackObserver()

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val message = binding.etMessage.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendFeedbackByEmailApi(name, email, message)
        }
    }

    private fun feedbackObserver() {
        feedbackViewModel.progressIndicator.observe(this) {

        }
        feedbackViewModel.feedbackResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        }
        feedbackViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@FeedbackActivity, it)
        }
    }

    private fun sendFeedbackByEmailApi(name: String, email: String, message: String) {
        val body = FeedbackBody(
            name = name,
            email = email,
            message = message
        )
        feedbackViewModel.feedbackApi(this, progressDialog, body)

    }

}