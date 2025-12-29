package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.travel.uzoefuapp.companyActivities.BookingProductActivity
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.FragmentStep4Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step4Fragment : Fragment() {
    private var _binding: FragmentStep4Binding? = null
    private val binding get() = _binding!!
    private var activityId: String? = null
    private var productName: String? = null
    private var paymentUrl: String? = null
    private var payRequestId: String? = null
    private var checksum: String? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    companion object {
        fun newInstance(
            activityId: String,
            productName: String,
            paymentUrl: String?,
            payRequestId: String,
            checksum: String
        ): Step4Fragment {
            val fragment = Step4Fragment()
            val args = Bundle().apply {
                putString("activityId", activityId)
                putString("productName", productName)
                putString("paymentUrl", paymentUrl)
                putString("payRequestId", payRequestId)
                putString("checksum", checksum)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep4Binding.inflate(inflater, container, false)

        arguments?.let {
            activityId = it.getString("activityId")
            productName = it.getString("productName")
            paymentUrl = it.getString("paymentUrl")
            payRequestId = it.getString("payRequestId")
            checksum = it.getString("checksum")
            saveDataToPrefs()
        }

        binding.activityName.text = productName

        setupWebView()
        return binding.root
    }

    private fun setupWebView() {

        val webView = binding.paymentWebView

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webChromeClient = WebChromeClient()

        progressDialog.start("")

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressDialog.start("")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressDialog.stop()
                Log.d("PaymentWebView", "Loaded: $url")

                url?.let {
                    when {
                        it.contains("success", true) -> {
                            Toast.makeText(
                                requireContext(),
                                "Payment Successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            view?.postDelayed({
                                goToDashboard()
                            }, 2000)
                        }

                        it.contains("cancel", true) ||
                                it.contains("failed", true) -> {
                            Toast.makeText(
                                requireContext(),
                                "Payment Failed or Cancelled",
                                Toast.LENGTH_SHORT
                            ).show()

                            view?.postDelayed({
                                goBackToBooking()
                            }, 2000)
                        }

                        else -> {}
                    }
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                progressDialog.stop()
                Log.e("PaymentWebView", "Error: ${error?.description}")
            }
        }

        // ✅ POST REQUEST (MOST IMPORTANT PART)
        if (!paymentUrl.isNullOrEmpty()
            && !payRequestId.isNullOrEmpty()
            && !checksum.isNullOrEmpty()
        ) {

            val postData =
                "PAY_REQUEST_ID=$payRequestId&CHECKSUM=$checksum"

            Log.d("PaymentPOST", "POST → $paymentUrl")
            Log.d("PaymentPOST", postData)

            webView.postUrl(
                paymentUrl!!,
                postData.toByteArray(Charsets.UTF_8)
            )

        } else {
            Toast.makeText(requireContext(), "Invalid payment data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToDashboard() {
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    private fun goBackToBooking() {
        val intent = Intent(requireContext(), BookingProductActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    private fun saveDataToPrefs() {
        val prefs = requireActivity().getSharedPreferences("BookingPrefs", 0)
        prefs.edit().apply {
            putString("activityId", activityId)
            putString("productName", productName)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog.stop()
        _binding = null
    }
}
