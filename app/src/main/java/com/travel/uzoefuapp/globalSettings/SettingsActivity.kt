package com.travel.uzoefuapp.globalSettings

import CustomProgressDialog
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.FeedbackActivity
import com.travel.uzoefuapp.activities.HelpCentreActivity
import com.travel.uzoefuapp.activities.LoginActivity
import com.travel.uzoefuapp.activities.SettingActivity
import com.travel.uzoefuapp.activities.TermAndConditionActivity
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.databinding.ActivitySettingsBinding
import com.travel.uzoefuapp.logoutModel.LogoutViewModel
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val logoutViewModel: LogoutViewModel by viewModels()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener { finish() }

        logoutObserver()
        notificationCountApi()
        notificationCountObserver()

        val webView = binding.webViewAboutUs
        setupWebView(webView)
        webView.loadUrl("https://mobappssolutions.in/uzoefu/aboutUs")

        binding.menuIcon.setOnClickListener { showSettingsBottomSheet() }

        showSettingsBottomSheet()

    }

    private fun setupWebView(webView: WebView) {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

        webView.webViewClient = WebViewClient()
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.isHorizontalScrollBarEnabled = false
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
        val shareLayout = view.findViewById<LinearLayout>(R.id.shareLayout)
        val signOutLayout = view.findViewById<LinearLayout>(R.id.signOutLayout)


        aboutLayout.setOnClickListener {
            val webView = binding.webViewAboutUs
            setupWebView(webView)
            webView.loadUrl("https://mobappssolutions.in/uzoefu/aboutUs")
            bottomSheetDialog.dismiss()
        }

        settingsLayout.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            // bottomSheetDialog.dismiss()
        }

        helpLayout.setOnClickListener {
            val intent = Intent(this, HelpCentreActivity::class.java)
            startActivity(intent)
            // bottomSheetDialog.dismiss()
        }

        feedbackLayout.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
            //  bottomSheetDialog.dismiss()
        }

        legalLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "terms")
            startActivity(intent)
            // bottomSheetDialog.dismiss()
        }

        shareLayout.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"

            // Replace with your app link or Google Play store link
            val appLink = "https://play.google.com/store/apps/details?id=${packageName}"

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey! I found this awesome app. Download it here:\n$appLink"
            )

            startActivity(Intent.createChooser(shareIntent, "Share app via"))

            bottomSheetDialog.dismiss()
        }


        signOutLayout.setOnClickListener {
            openLogoutCustomPopup()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }


    private fun logoutObserver() {
        logoutViewModel.progressIndicator.observe(this) {
        }

        logoutViewModel.mRegisterResponse.observe(this) { response ->
            val success = response.peekContent().status
            val message = response.peekContent().message

            if (success == true) {
                Uzoefu.encryptedPrefs.bearerToken = ""
                Uzoefu.encryptedPrefs.isNotification = false
                Uzoefu.encryptedPrefs.isFirstTime = false

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        logoutViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun openLogoutCustomPopup() {
        val dialogView = layoutInflater.inflate(R.layout.logout_popup, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            dialog.dismiss()
            logoutApi()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun logoutApi() {
        logoutViewModel.userLogoutApi(this)
    }

}