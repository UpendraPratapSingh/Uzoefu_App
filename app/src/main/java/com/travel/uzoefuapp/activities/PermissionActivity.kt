package com.travel.uzoefuapp.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import android.provider.Settings
import com.travel.uzoefuapp.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding
    private lateinit var btnGrant: TextView
    private lateinit var arrowBack: ImageView

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fine || coarse) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Check if user selected "Don't ask again"
                val showRationale = shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                if (!showRationale) {
                    // User permanently denied
                    showPermissionDeniedDialog()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Needed")
            .setMessage("Location permission is permanently denied. Please enable it in App Settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnGrant = findViewById(R.id.btnDeleteAccount)
        arrowBack = findViewById(R.id.arrowBack)

        arrowBack.setOnClickListener {
            onBackPressed()
        }
        btnGrant.setOnClickListener { requestLocationPermission() }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}