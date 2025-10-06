package com.travel.uzoefuapp.companyActivities

import CustomProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistViewModel
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Action
import com.travel.uzoefuapp.adapter.ActionAdapter
import com.travel.uzoefuapp.adapter.ProductSliderAdapter
import com.travel.uzoefuapp.adapter.ProductTabAdapter
import com.travel.uzoefuapp.adapter.SliderAdapter
import com.travel.uzoefuapp.bookingActivities.BookingDetailStep1Activity
import com.travel.uzoefuapp.databinding.ActivityBookingProductBinding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator3

@AndroidEntryPoint
class BookingProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookingProductBinding
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: CircleIndicator3
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val addWishlistViewModel: AddWishlistViewModel by viewModels()
    private var categoryId: Int = -1
    private var activeHour = ""

    private val slideRunnable = object : Runnable {
        override fun run() {
            val itemCount = viewPager.adapter?.itemCount ?: 0
            if (itemCount > 0) {
                currentPage = (currentPage + 1) % itemCount
                viewPager.setCurrentItem(currentPage, true)
            }
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        this.window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            statusBarColor = Color.TRANSPARENT
        }

        binding.btnBack.setOnClickListener { finish() }

        categoryId = intent.getIntExtra("categoryId", -1)
        //activeHour = intent.getStringExtra("activeHours").toString()

        Log.e("TAG", "onCreate: $activeHour")

        getDetailApi(categoryId)
        getDetailObserver()
        activityAddToWishListObserver()

        binding.btnMore.setOnClickListener {
            val intent = Intent(this@BookingProductActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        /*  binding.tvCategory.setOnClickListener {
              val intent = Intent(this@BookingProductActivity, CompanyLandingActivity::class.java)
              startActivity(intent)
          }*/

        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.dotsIndicator)

        /*  val images = listOf(R.drawable.balloonslide, R.drawable.balloonslide, R.drawable.balloonslide)

          viewPager.adapter = SliderAdapter(images)
          indicator.setViewPager(viewPager)*/

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })

        binding.productRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.actionRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val actions = listOf(
            Action(R.drawable.ic_call, "Call"),
            Action(R.drawable.ic_shared, "Map"),
            Action(R.drawable.ic_copy, "Add to Trip"),
            Action(R.drawable.ic_shared, "Share")
        )

        val actionAdapter = ActionAdapter(actions) { action ->
            when (action.label) {
                "Call" -> {
                    val phone = +27634854147
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phone")
                    }
                    startActivity(intent)
                }

                "Map" -> {
                    val location = "5467 Gymnema  St Waterberg Fields estate Kosmosdal, Centurion"
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("geo:0,0?q=$location")
                    }
                    startActivity(intent)
                }

                "Add to Trip" -> {
                    Toast.makeText(this, "${action.label} added to trip!", Toast.LENGTH_SHORT)
                        .show()
                }

                "Share" -> {
                    val shareText = "Visit the Taj Mahal!"
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    startActivity(Intent.createChooser(intent, "Share via"))
                }
            }
        }

        binding.actionRecyclerView.adapter = actionAdapter

        val adapter = ProductTabAdapter(this, categoryId, activeHour)
        binding.viewPagerData.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerData) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Information"
                2 -> "Reviews"
                3 -> "FAQ"
                else -> ""
            }
        }.attach()

        binding.viewPagerData.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.button2.visibility = if (position == 2) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        })
    }

    private fun activityAddToWishListObserver() {
        addWishlistViewModel.progressIndicator.observe(this) {

        }
        addWishlistViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                getDetailApi(categoryId)
            }
        }
        addWishlistViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }


    private fun getDetailObserver() {
        detailPageViewModel.progressIndicator.observe(this) {

        }
        detailPageViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data1 = response.peekContent().data?.activity
            val data2 = response.peekContent().data?.activity?.category
            val data3 = response.peekContent().data?.price
            //val activityId = data3?.activityId
            val address = data1?.branch
            val isWish = response.peekContent().data?.iswish
            activeHour = response.peekContent().data?.todayHours.toString()
            val activityId = data1?.id

            if (success == true) {
                binding.tvTitle.text = data1?.activityName.toString()
                binding.tvCategory.text = data2?.name.toString()
                //   binding.tvPrice.text = "R ${data3?.groupPrice.toString()}"
                //  binding.tvPrice.text = "R ${"%.2f".format(data3?.groupPrice ?: 0.0)}"
                binding.tvPrice.text = "R ${data3?.groupPrice ?: 0}"

                val images = response.peekContent().data?.images ?: emptyList()

                val thumbnailAdapter = ProductSliderAdapter(images) { position ->

                }
                binding.productRecyclerView.adapter = thumbnailAdapter

                /*     binding.button2.setOnClickListener {
                         val intent = Intent(this@BookingProductActivity, BookingDetailStep1Activity::class.java)
                         intent.putExtra("price", data3?.groupPrice.toString())
                         intent.putExtra("childrenPrice", data3?.childrenBase.toString())
                         intent.putExtra("activityId", data1?.id.toString())
                         intent.putExtra("productName", data1?.activityName.toString())
                         intent.putExtra("address", address?.address.toString())
                         intent.putExtra("town", address?.town.toString())
                         startActivity(intent)

                     }*/

                binding.button2.setOnClickListener {
                    val price = data3?.groupPrice ?: 0.0

                    if (price == 0.0) {
                        Toast.makeText(
                            this,
                            "This activity cannot be booked (price is 0)",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent = Intent(
                            this@BookingProductActivity,
                            BookingDetailStep1Activity::class.java
                        )
                        intent.putExtra("price", data3?.groupPrice.toString())
                        intent.putExtra("childrenPrice", data3?.childrenBase.toString())
                        intent.putExtra("activityId", data1?.id.toString())
                        intent.putExtra("productName", data1?.activityName.toString())
                        intent.putExtra("address", address?.address.toString())
                        intent.putExtra("town", address?.town.toString())
                        startActivity(intent)
                    }
                }

                viewPager.adapter = SliderAdapter(images)
                indicator.setViewPager(viewPager)



                binding.iconFav.setImageResource(
                    if (isWish == true) R.drawable.wishlist_color
                    else R.drawable.ic_wish
                )

                binding.iconFav.setOnClickListener { view ->
                    view.animate()
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .setDuration(150)
                        .withEndAction {
                            view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)
                                .start()
                        }
                        .start()

                    activityId?.let { addToWishlistApi(it) }
                }
            }
        }
        detailPageViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@BookingProductActivity, it)
        }
    }

    private fun addToWishlistApi(id: Int?) {
        val body = AddWishlistBody(activity_id = id.toString())
        addWishlistViewModel.addToWishListApi(progressDialog, this, body)
    }


    private fun getDetailApi(categoryId: Int) {
        val body = DetailPageBody(
            activity_id = categoryId.toString()
        )
        detailPageViewModel.getDetailPageApi(progressDialog, this, body)

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(slideRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(slideRunnable)
    }
}