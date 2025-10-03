package com.travel.uzoefuapp.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityTermAndConditionBinding

class TermAndConditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermAndConditionBinding
    private lateinit var webView: WebView
    private var condition =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTermAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageView2.setOnClickListener { finish() }

        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        webView.loadUrl("https://mobappssolutions.in/uzoefu/termcondition")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}