package com.travel.uzoefuapp.companyActivities

import CustomProgressDialog
import android.content.Context
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
import androidx.core.view.WindowInsetsControllerCompat
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
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.bookingActivities.BookingDetailStep1Activity
import com.travel.uzoefuapp.databinding.ActivityBookingProductBinding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.userShareReward.UserShareRewardBody
import com.travel.uzoefuapp.userShareReward.UserShareRewardViewModel
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
    private val userShareRewardViewModel: UserShareRewardViewModel by viewModels()
    private var categoryId: Int = -1
    private var activeHour = ""
    private var location = ""
    private var telePhone = ""
    private var adviceId = ""

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

        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            statusBarColor = Color.TRANSPARENT

            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = false
        }

        binding.btnBack.setOnClickListener { finish() }

        categoryId = intent.getIntExtra("categoryId", -1)

        /*        if (categoryId != -1) {
                    getDetailApi(categoryId)
                    Log.e("TAGAAAAAAA", "onCreateAAAAAAA: $categoryId")
                } else {
                    // Deep link handle karo
                    var data: Uri? = intent?.data
                    if (data != null) {
                        val adviceId = data.lastPathSegment // "222" milega
                        Toast.makeText(this, "Advice ID: $adviceId", Toast.LENGTH_SHORT).show()
                        *//*   val intent = Intent(this, GearzzoneAdviceDetailsActivity::class.java)
                   intent.putExtra("adviceId", adviceId)
                   startActivity(intent)*//*
                // Ab yaha API call karke details dikha sakte ho
                if (adviceId != null) {
                    getDetailApi(adviceId.toInt())
                    Log.e("TAGAAAAAAA", "onCreateBBBBBBBBBBBB: $adviceId")

                }
            }
        }*/

        val categoryId = intent.getIntExtra("categoryId", -1)

        /*   if (categoryId != -1) {
               // Normal case — open using category ID
               getDetailApi(categoryId)
               Log.e("TAG", "Normal Launch → categoryId: $categoryId")
           } else {
               // Deep link handling
               val data: Uri? = intent?.data
               if (data != null) {
                   val adviceIdStr = data.lastPathSegment
                   Log.d("DeepLink", "Raw URI: $data, adviceIdStr: $adviceIdStr")

                   adviceIdStr?.toIntOrNull()?.let { adviceId ->
                       Toast.makeText(this, "Advice ID: $adviceId", Toast.LENGTH_SHORT).show()
                       getDetailApi(adviceId)
                       Log.e("TAG", "Deep Link Launch → adviceId: $adviceId")
                   } ?: run {
                       Log.e("TAG", "Invalid adviceId in deep link: $data")
                   }
               } else {
                   Log.e("TAG", "No deep link data found")
               }
           }*/
        handleIntent(intent)


        getDetailObserver()
        activityAddToWishListObserver()
        shareRewardPointObserver()


        binding.btnMore.setOnClickListener {
            val intent = Intent(this@BookingProductActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.dotsIndicator)

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
            Action(R.drawable.mapicon, "Map"),
            Action(R.drawable.share, "Share")
        )

        val actionAdapter = ActionAdapter(actions) { action ->
            when (action.label) {
                "Call" -> {
                    val phone = telePhone
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phone")
                    }
                    startActivity(intent)
                }

                "Map" -> {
                    val location = location
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
                    shareAdctivity(this, categoryId.toString())
                    Log.e("TAGAAAAAA", "onCreate: $categoryId")
                }
            }
        }

        binding.actionRecyclerView.adapter = actionAdapter

        if (categoryId != -1) {
            val adapter = ProductTabAdapter(this, categoryId, activeHour)
            binding.viewPagerData.adapter = adapter
        } else {
            val adapter = ProductTabAdapter(this, adviceId.toInt(), activeHour)
            binding.viewPagerData.adapter = adapter
        }


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

    private fun shareRewardPointObserver() {
        userShareRewardViewModel.progressIndicator.observe(this) {

        }
        userShareRewardViewModel.userShareRewardResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }

        }
        userShareRewardViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@BookingProductActivity, it)
        }

    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return

        val categoryId = intent.getIntExtra("categoryId", -1)
        val data: Uri? = intent.data

        when {
            data != null -> { // Deep link launch
                val pathSegments = data.pathSegments
                adviceId = pathSegments[1].toIntOrNull().toString()
                val userId = pathSegments[2]
                //adviceId = data.lastPathSegment?.toIntOrNull().toString()
                adviceId = data.lastPathSegment?.get(1).toString() // "222"

                //val userId = data.lastPathSegment?.get(2).toString()
                if (adviceId != null) {
                    //  Log.d("Gearzzone", "DeepLink launch → adviceId: $adviceId")
                    //Toast.makeText(this, "Activity ID: $adviceId", Toast.LENGTH_SHORT).show()
                    getDetailApi(adviceId.toInt())
                    shareRewardPointApi(userId)
                } else {
                    Log.e("Gearzzone", "Invalid adviceId in deep link: $data")
                }
            }

            categoryId != -1 -> {
                getDetailApi(categoryId)
            }

            else -> {
                Log.e("Gearzzone", "No valid ID found in intent")
            }
        }
    }

    private fun shareRewardPointApi(userId: String) {
        val body = UserShareRewardBody(
            userId = userId
        )
        userShareRewardViewModel.userShareRewardListApi(this, progressDialog, body)

    }

    private fun shareAdctivity(context: Context, adviceId: String) {
        val userId = Uzoefu.encryptedPrefs.userId
        val shareLink = "https://uzoefu.co.za/reward/$adviceId/$userId"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check this advice on GearZ")
        intent.putExtra(Intent.EXTRA_TEXT, shareLink)
        context.startActivity(Intent.createChooser(intent, "Share via"))
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
            val address = data1?.branch
            val isWish = response.peekContent().data?.iswish
            activeHour = response.peekContent().data?.todayHours.toString()
            val activityId = data1?.id

            location = response.peekContent().data?.activity?.branch?.address.toString()

            telePhone = response.peekContent().data?.activity?.branch?.teliphoneNumber.toString()

            if (success == true) {
                binding.main.visibility = View.VISIBLE
                binding.tvTitle.text = data1?.activityName.toString()
                binding.tvCategory.text = data2?.name.toString()
                binding.tvPrice.text = "R ${data3?.groupPrice ?: 0}"

                val images = response.peekContent().data?.images ?: emptyList()

                val thumbnailAdapter = ProductSliderAdapter(images) { _ -> }

                binding.productRecyclerView.adapter = thumbnailAdapter

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
            } else {
                binding.main.visibility = View.GONE
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

    // ✅ Handle if activity is relaunched due to deep link (singleTop mode)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }
}