package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SupportActivity
import com.travel.uzoefuapp.activities.TermAndConditionActivity
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.bookingCompleteModel.BookingDetailBody
import com.travel.uzoefuapp.bookingCompleteModel.BookingDetailViewModel
import com.travel.uzoefuapp.databinding.ActivityBookSummaryBinding
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSummaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookSummaryBinding
    private val bookingDetailViewModel: BookingDetailViewModel by viewModels()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var bookingId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bookingId = intent.getStringExtra("bookingId").toString()

        bookingDetailApi(bookingId)
        bookingDetailObserver()
        notificationCountApi()
        notificationCountObserver()

        binding.imageView2.setOnClickListener { finish() }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookSummaryActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            showSettingsBottomSheet()
        }

        binding.downloadSignedIndemnity.setOnClickListener { downloadInvoice() }

        binding.downloadReceipt.setOnClickListener { downloadInvoice1() }

        binding.downloadInvoice.setOnClickListener { downloadInvoice1() }

    }

    @SuppressLint("MissingInflatedId")
    private fun showSettingsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_settings, null)
        bottomSheetDialog.setContentView(view)

        val aboutLayout = view.findViewById<LinearLayout>(R.id.aboutLayout)
        val settingsLayout = view.findViewById<LinearLayout>(R.id.settingsLayout)
        val helpLayout = view.findViewById<LinearLayout>(R.id.helpLayout)
        val feedbackLayout = view.findViewById<LinearLayout>(R.id.feedbackLayout)
        val legalLayout = view.findViewById<LinearLayout>(R.id.legalLayout)
        val referralLayout = view.findViewById<LinearLayout>(R.id.referralLayout)

        aboutLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "terms")
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        settingsLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "privacy")
            startActivity(intent)
        }

        helpLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "refund")

            startActivity(intent)
        }

        feedbackLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "faq")
            startActivity(intent)
        }

        legalLayout.setOnClickListener {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
        }

        referralLayout.setOnClickListener {
            val referCode = Uzoefu.encryptedPrefs.statusDone
            //val referLink = "https://yourapp.com/referral?code=$referCode"
            val referLink = "https://uzoefu.co.za/reward/$referCode"

            Log.e("referralCode", "showSettingsBottomSheetAAAAAAAAAAAA $referCode")

            val shareMessage = """
                
        🎉✨ **Exclusive Offer Just for You!** ✨🎉
        
        Hey there! I’ve been using **Uzoefu App**, and it’s been an amazing experience.  
        You can now join too — and guess what? You’ll get **₹150 bonus** just for signing up! 💰
        
        🔹 Here’s how it works:
        1️⃣ Click on the link below to download or open the app  
        2️⃣ Sign up using my referral code: **$referCode**  
        3️⃣ You’ll instantly receive your reward once you complete your first activity! 🚀
        
        💡 **Why you’ll love Uzoefu App:**
        - Easy and secure to use  
        - Exciting rewards for every action  
        - Trusted by thousands of happy users  
        - Quick payouts and referral bonuses  

        👉 Tap the link now to get started:  
        $referLink

        🌟 Don’t miss this chance — invite your friends and earn together! 🌟
        
        — Sent via Uzoefu ❤️
        
    """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Invite & Earn ₹150 with Uzoefu App")
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }

            startActivity(Intent.createChooser(intent, "Share via"))
        }
        bottomSheetDialog.show()
    }

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                val count = data ?: 0

                if (count == 0) {
                    binding.notificationBadge.visibility = View.GONE
                } else {
                    binding.notificationBadge.visibility = View.VISIBLE
                    binding.notificationBadge.text = count.toString()
                }

            }

        }
        notificationCountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(this, progressDialog)

    }

    private fun downloadInvoice() {
        val url = "https://uzoefu.co.za/indeminity/form?id=$bookingId"
        val fileName = "invoice.pdf"
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Invoice")
            .setDescription("Downloading your invoice...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun downloadInvoice1() {
        val url = "https://uzoefu.co.za/invoice/form?id=$bookingId"
        val fileName = "invoice.pdf"
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Invoice")
            .setDescription("Downloading your invoice...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun bookingDetailObserver() {
        bookingDetailViewModel.progressIndicator.observe(this) {

        }
        bookingDetailViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val data = response.peekContent().data?.bookingDetail

            if (success == true) {
                binding.bookingSummaryLayout.visibility = View.VISIBLE
                binding.activityName.text = data?.activityName
                binding.userName.text = data?.contactName
                binding.selectedDate.text = data?.bookingDate
                binding.refrenceId.text = data?.paymentId
                binding.contactName.text = data?.contactNumber
                binding.email.text = data?.email
                binding.addresss.text = data?.billingAddress
                binding.adultAmount.text = "R${data?.adultAmount}"
                binding.kidsAmount.text = "R${data?.kidsAmount}"
                binding.adultQuantity.text = data?.adultQty.toString()
                binding.kidsQuantity.text = data?.kidsQty.toString()
                binding.adultDescription.text = data?.activityName
                binding.kidsDescription.text = data?.activityName
                binding.subTotal.text = "R${data?.subtotal}"
                binding.totalPrice.text = "R${data?.total}"

            } else {
                binding.bookingSummaryLayout.visibility = View.GONE
            }
        }
        bookingDetailViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@BookSummaryActivity, it)
        }
    }

    private fun bookingDetailApi(bookingId: String?) {
        val body = BookingDetailBody(
            booking_id = bookingId.toString()
        )
        bookingDetailViewModel.bookingDetailApi(progressDialog, this, body)

    }
}