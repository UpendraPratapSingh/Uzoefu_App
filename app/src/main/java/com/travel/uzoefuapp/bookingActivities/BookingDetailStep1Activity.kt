package com.travel.uzoefuapp.bookingActivities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingDetailFragment.Step1Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step2Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step3Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step4Fragment
import com.travel.uzoefuapp.bookingDetailFragment.Step5Fragment
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.ActivityBookingDetailStep1Binding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingDetailStep1Activity : AppCompatActivity() {
    lateinit var binding: ActivityBookingDetailStep1Binding
    private lateinit var steps: List<TextView>
    private lateinit var lines: List<View>
    private lateinit var nextBtn: Button
    private var currentStep = 1
    var price = ""
    private var childrenPrice = ""
    private var activityId = ""
    private var address = ""
    private var town = ""
    private var productName = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingDetailStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get values from intent
        price = intent.getStringExtra("price").toString()
        childrenPrice = intent.getStringExtra("childrenPrice").toString()
        activityId = intent.getStringExtra("activityId").toString()
        town = intent.getStringExtra("town").toString()
        address = intent.getStringExtra("address").toString()
        productName = intent.getStringExtra("productName").toString()

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookingDetailStep1Activity, NotificationActivity::class.java)
            startActivity(intent)
        }

        steps = listOf(
            findViewById(R.id.step1),
            findViewById(R.id.step2),
            findViewById(R.id.step3),
            findViewById(R.id.step4),
            findViewById(R.id.step5)
        )

        lines = listOf(
            findViewById(R.id.line1),
            findViewById(R.id.line2),
            findViewById(R.id.line3),
            findViewById(R.id.line4)
        )

        nextBtn = findViewById(R.id.nextButton)

        binding.btnBack.setOnClickListener { finish() }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@BookingDetailStep1Activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        openFragment(Step1Fragment.newInstance(price, childrenPrice, activityId, address, town, productName))
        updateStepper()

        nextBtn.setOnClickListener {
            if (currentStep < 5) {
                currentStep++
                when (currentStep) {
                    2 -> openFragment(Step2Fragment())
                    3 -> openFragment(Step3Fragment(activityId))
                    4 -> openFragment(Step4Fragment())
                    5 -> openFragment(Step5Fragment())
                }
                updateStepper()
            } else {
                val intent = Intent(this@BookingDetailStep1Activity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.tripFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun updateStepper() {
        for (i in steps.indices) {
            if (i < currentStep) {
                steps[i].setBackgroundResource(R.drawable.circle_active)
                steps[i].setTextColor(resources.getColor(R.color.green_color, theme))
            } else {
                steps[i].setBackgroundResource(R.drawable.circle_inactive)
                steps[i].setTextColor(resources.getColor(android.R.color.darker_gray, theme))
            }
        }

        for (i in lines.indices) {
            if (i < currentStep) {
                lines[i].setBackgroundResource(R.color.line_active)
            } else {
                lines[i].setBackgroundResource(R.color.line_inactive)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (currentStep > 1) {
            currentStep--
            when (currentStep) {
                1 -> openFragment(Step1Fragment.newInstance(
                    price,
                    childrenPrice,
                    activityId,
                    address,
                    town,
                    productName
                ))
                2 -> openFragment(Step2Fragment())
                3 -> openFragment(Step3Fragment(activityId))
                4 -> openFragment(Step4Fragment())
            }
            updateStepper()
        } else {
            finish()
        }
    }
}