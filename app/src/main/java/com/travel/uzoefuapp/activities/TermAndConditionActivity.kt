package com.travel.uzoefuapp.activities

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.travel.uzoefuapp.databinding.ActivityTermAndConditionBinding

class TermAndConditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermAndConditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView2.setOnClickListener { finish() }

        val webView = binding.webView
        setupWebView(webView)

        val pageType = intent.getStringExtra("page_type") ?: "terms"
        when (pageType) {
            "privacy" -> {
                binding.headerTitle.text = "Privacy and Policy"
                webView.loadUrl("https://uzoefu.co.za/privacy/policy?for=app")
            }

            "refund" -> {
                binding.headerTitle.text = "Refund and Cancellation Policy"
                webView.loadUrl("https://uzoefu.co.za/refund-cancellation-policies?for=app")
            }

            "faq" -> {
                binding.headerTitle.text = "FAQ"
                webView.loadUrl("https://uzoefu.co.za/faq?for=app")
            }

            else -> {
                binding.headerTitle.text = "Terms & Conditions"
                webView.loadUrl("https://uzoefu.co.za/termcondition?for=app")
            }
        }
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

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}


