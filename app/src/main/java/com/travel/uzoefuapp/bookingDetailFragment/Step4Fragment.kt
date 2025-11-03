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
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.bookingActivities.BookingDetailStep1Activity
import com.travel.uzoefuapp.companyActivities.BookingProductActivity
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.FragmentStep4Binding
import com.travel.uzoefuapp.paymentModel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step4Fragment : Fragment() {

    private var _binding: FragmentStep4Binding? = null
    private val binding get() = _binding!!

    private var activityId: String? = null
    private var productName: String? = null
    private var paymentUrl: String? = null
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    companion object {
        fun newInstance(
            activityId: String,
            productName: String,
            paymentUrl: String?
        ): Step4Fragment {
            val fragment = Step4Fragment()
            val args = Bundle().apply {
                putString("activityId", activityId)
                putString("productName", productName)
                putString("paymentUrl", paymentUrl)
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
            saveDataToPrefs()
        }

        setupWebView()
        return binding.root
    }

    private fun setupWebView() {
        val webView: WebView = binding.paymentWebView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        progressDialog.start("")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (isAdded && activity != null && !requireActivity().isFinishing) {
                    progressDialog.start()
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (isAdded && activity != null && !requireActivity().isFinishing) {
                    progressDialog.stop()
                }
            }

            /*
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            url?.let {
                                Log.d("PaymentWebView", "Loading URL: $it")

                                when {
                                    it.contains("success", true) -> {
                                        progressDialog.stop()
                                        goToStep5()
                                    }

                                    it.contains("failure", true) || it.contains("cancel", true) -> {
                                        progressDialog.stop()
                                        goToStep5()
                                    }

                                    else -> view?.loadUrl(it)
                                }
                            }
                            return true
                        }
            */

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    Log.d("PaymentWebView", "Loading URL: $it")

                    when {
                        it.contains("success", true) ||
                                it.contains("payment/success", true) ||
                                it.contains("booking/confirmation", true) -> {
                            progressDialog.stop()
                            Log.e("Success", "Payment success detected")

                            // ✅ Add 2-second delay before navigating to Step5
                            view?.postDelayed({
                                if (isAdded && isResumed) {
                                    goToStep5()
                                }
                            }, 2000)

                            return true
                        }

                        it.contains("failure", true) ||
                                it.contains("cancel", true) ||
                                it.contains("payment/failed", true) -> {
                            progressDialog.stop()
                            Toast.makeText(requireContext(),
                                "Payment failed or cancelled",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(requireContext(), BookingProductActivity::class.java)
                            startActivity(intent)
                            return true
                        }

                        else -> view?.loadUrl(it)
                    }
                }
                return true
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                progressDialog.stop()
                Log.e("PaymentWebView", "Error: $description, URL: $failingUrl")
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

        webView.webChromeClient = WebChromeClient()

        // Load the payment URL
        paymentUrl?.let {
            Log.d("PaymentURL", "Loading Payment URL: $it")
            webView.loadUrl(it)
        } ?: run {
            Log.e("PaymentURL", "Invalid or empty payment URL")
        }
    }

    private fun goToStep5() {
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        intent.putExtra("goToStep", 5)
        intent.putExtra("productName", productName)
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
        _binding = null
        progressDialog.stop()
    }
}
