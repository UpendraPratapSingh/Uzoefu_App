package com.travel.uzoefuapp.bookingActivities

import android.annotation.SuppressLint
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
import com.travel.uzoefuapp.databinding.ActivityBookingDetailStep1Binding

class BookingDetailStep1Activity : AppCompatActivity() {
    lateinit var binding: ActivityBookingDetailStep1Binding
    private lateinit var steps: List<TextView>
    private lateinit var lines: List<View>
    private lateinit var nextBtn: Button

    private var currentStep = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingDetailStep1Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_booking_detail_step1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener { finish() }

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

        openFragment(Step1Fragment())
        updateStepper()

        nextBtn.setOnClickListener {
            if (currentStep < 5) {
                currentStep++
                when (currentStep) {
                    2 -> openFragment(Step2Fragment())
                    3 -> openFragment(Step3Fragment())
                    4 -> openFragment(Step4Fragment())
                    5 -> openFragment(Step5Fragment())
                }
                updateStepper()
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
                steps[i].setTextColor(resources.getColor(android.R.color.white, theme))
            } else {
                steps[i].setBackgroundResource(R.drawable.circle_inactive)
                steps[i].setTextColor(resources.getColor(android.R.color.darker_gray, theme))
            }
        }

        for (i in lines.indices) {
            if (i < currentStep - 1) {
                lines[i].setBackgroundResource(R.color.line_active)
            } else {
                lines[i].setBackgroundResource(R.color.line_inactive)
            }
        }
    }
}