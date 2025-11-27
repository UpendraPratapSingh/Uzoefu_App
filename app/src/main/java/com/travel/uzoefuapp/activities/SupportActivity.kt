package com.travel.uzoefuapp.activities

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.ActivitySupportBinding
import com.travel.uzoefuapp.supportModel.SupportBody
import com.travel.uzoefuapp.supportModel.SupportViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySupportBinding
    private val supportViewModel: SupportViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageView2.setOnClickListener { finish() }

        //call observer
        supportObserver()

        val reasons = listOf(
            "Compliment",
            "Complaint",
            "Booking Issue",
            "Payment Issue",
            "Other"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reasons)
        binding.reasonForContact.setAdapter(adapter)

        // Fix: dropdown should open when clicking anywhere
        binding.reasonForContact.setOnClickListener {
            binding.reasonForContact.showDropDown()
        }

        binding.btnSubmit.setOnClickListener {
            if (validateInputs()) {
                callSupportApi()
            }
        }
    }

    private fun supportObserver() {
        supportViewModel.progressIndicator.observe(this) {

        }
        supportViewModel.userShareRewardResponse.observe(this) {
            val success = it.peekContent().success
            val message = it.peekContent().message

            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SupportActivity, DashboardActivity::class.java)
                startActivity(intent)
            }
        }

        supportViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SupportActivity, it)
        }
    }

    private fun callSupportApi() {
        val firstName = binding.etInput.text.toString().trim()
        val lastName = binding.etInputLastName.text.toString().trim()
        val email = binding.etInputEmail.text.toString().trim()
        val phoneWithCode =
            binding.countryCode.selectedCountryCodeWithPlus + binding.etPhone.text.toString().trim()
        val reason = binding.reasonForContact.text.toString().trim()
        val comment = binding.etComment.text.toString().trim()

        val body = SupportBody(
            firstname = firstName,
            lastname = lastName,
            email_address = email,
            number = phoneWithCode,
            reason = reason,
            comment = comment
        )
        supportViewModel.supportApi(this, progressDialog, body)
    }

    private fun validateInputs(): Boolean {
        val firstName = binding.etInput.text.toString().trim()
        val lastName = binding.etInputLastName.text.toString().trim()
        val email = binding.etInputEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val reason = binding.reasonForContact.text.toString().trim()

        if (firstName.isEmpty()) {
            binding.etInput.error = "First name required"
            binding.etInput.requestFocus()
            return false
        }
        if (lastName.isEmpty()) {
            binding.etInputLastName.error = "Last name required"
            binding.etInputLastName.requestFocus()
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etInputEmail.error = "Valid email required"
            binding.etInputEmail.requestFocus()
            return false
        }
        if (phone.isEmpty() || phone.length < 6) {
            binding.etPhone.error = "Valid phone number required"
            binding.etPhone.requestFocus()
            return false
        }
        if (reason.isEmpty()) {
            binding.reasonForContact.error = "Please select a reason"
            binding.reasonForContact.requestFocus()
            return false
        }
        return true
    }
}