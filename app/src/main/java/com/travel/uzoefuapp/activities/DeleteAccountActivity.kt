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
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.databinding.ActivityDeleteAccountBinding
import com.travel.uzoefuapp.deleteAccountModel.DeleteAccountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteAccountBinding
    private val deleteAccountViewModel: DeleteAccountViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //call observer
        deleteAccountObserver()

        binding.arrowBack.setOnClickListener { finish() }

        binding.btnDeleteAccount.setOnClickListener {
            //call api
            deleteAccountApi()
        }
    }

    private fun deleteAccountObserver() {
        deleteAccountViewModel.progressIndicator.observe(this) {

        }
        deleteAccountViewModel.userShareRewardResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Uzoefu.encryptedPrefs.bearerToken = ""
                Uzoefu.encryptedPrefs.isNotification = false
                Uzoefu.encryptedPrefs.isFirstTime = false

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        deleteAccountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@DeleteAccountActivity, it)
        }
    }

    private fun deleteAccountApi() {
        deleteAccountViewModel.deleteAccount(this, progressDialog)
    }
}