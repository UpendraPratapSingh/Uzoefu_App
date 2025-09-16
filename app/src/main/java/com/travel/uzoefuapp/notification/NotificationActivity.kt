package com.travel.uzoefuapp.notification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.NotificationAdapter
import com.travel.uzoefuapp.adapter.NotificationItem
import com.travel.uzoefuapp.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageView2.setOnClickListener { finish() }

        val notifications = listOf(

            NotificationItem(
                "alert",
                "Alert",
                "Alert : Weather warning in effect. Check your travel plans!",
                "25min"
            ),

            NotificationItem(
                "feature",
                "New Feature",
                "This could announce a promotion as well",
                "2hr"
            ),

            NotificationItem(
                "booking",
                "Booking ",
                "New booking for Highlanders Cable Car Rides",
                "1hr"
            ),

            NotificationItem(
                "recommendation",
                "Recommendation",
                "You're near the city park! A free yoga class is starting in 15 minutes.",
                "15min"
            )
        )

        binding.notificationRecyclerView.layoutManager = GridLayoutManager(this, 1)
        binding.notificationRecyclerView.adapter = NotificationAdapter(notifications)

    }
}