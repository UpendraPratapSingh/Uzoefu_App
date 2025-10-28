package com.travel.uzoefuapp.activities

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.CategoriesAdapter
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.categoryModel.CategoryViewModel
import com.travel.uzoefuapp.databinding.ActivityExploreCategoriesBinding
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreCategoriesActivity : AppCompatActivity() {
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    lateinit var binding: ActivityExploreCategoriesBinding
    var data: List<CategoryResponse.Datum> = ArrayList()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExploreCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //observer call
        getCategoryApi()
        getCategoryObserver()
        notificationCountApi()
        notificationCountObserver()

        binding.forYouArrowImg.setOnClickListener { finish() }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@ExploreCategoriesActivity, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
            val success = response.peekContent().success
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

    private fun getCategoryObserver() {
        categoryViewModel.progressIndicator.observe(this) {}

        categoryViewModel.mCategoryResponse.observe(this) { event ->
            val content = event.peekContent()
            val success = content.success
            val message = content.message
            data = content.data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.categoriesRecycler.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                } else {
                    binding.noDataText.visibility = View.GONE
                    binding.categoriesRecycler.visibility = View.VISIBLE
                    binding.categoriesRecycler.layoutManager =
                        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                    val categoryAdapter = CategoriesAdapter(this, data)
                    binding.categoriesRecycler.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        categoryViewModel.errorResponse.observe(this) { error ->
            ErrorUtil.handlerGeneralError(this@ExploreCategoriesActivity, error)
        }
    }

    private fun getCategoryApi() {
        categoryViewModel.getCategory(progressDialog, this)

    }
}