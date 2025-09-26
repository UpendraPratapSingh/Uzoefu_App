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
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistViewModel
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.activityModl.ActivityViewModel
import com.travel.uzoefuapp.adapter.ExploreResultAdapter
import com.travel.uzoefuapp.adapter.OnWishlistListener
import com.travel.uzoefuapp.adapter.SelectedDestinationAdapter
import com.travel.uzoefuapp.databinding.ActivityExploreBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreActivity : AppCompatActivity(), OnWishlistListener {
    lateinit var binding: ActivityExploreBinding
    private val activityViewModel: ActivityViewModel by viewModels()
    private val categoryId = ""
    var data: List<ActivityResponse.Datum> = ArrayList()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val addWishlistViewModel: AddWishlistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forYouArrowImg.setOnClickListener { finish() }
        val experienceActivity = intent.getStringExtra("ExperienceActivity")

        if (experienceActivity == "1") {
            binding.resultCons.visibility = View.GONE
        } else {
            binding.resultCons.visibility = View.VISIBLE
        }
        getActivityApi(categoryId)
        getActivityObserver()

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@ExploreActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@ExploreActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        /*   binding.categoriesRecycler.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = ExploreResultAdapter(this)*/

    }

    private fun getActivityObserver() {
        activityViewModel.progressIndicator.observe(this) {}
        activityViewModel.categoryActivitiesResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            data = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.destinationRecycler.visibility = View.GONE
                } else {
                    binding.destinationRecycler.visibility = View.VISIBLE
                    val limitedList = data.take(2)
                    binding.destinationRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = SelectedDestinationAdapter(this, limitedList, this)
                    binding.destinationRecycler.adapter = categoryAdapter

                    val remainingList = data.drop(2)
                    binding.categoriesRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val verticalAdapter = ExploreResultAdapter(this, remainingList, this)
                    binding.categoriesRecycler.adapter = verticalAdapter

                }
            } else {
                Toast.makeText(this, message ?: "Failed to load categories", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        activityViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun getActivityApi(categoryId: String) {
        val body = ActivityBody(categoryId = categoryId)
        activityViewModel.getActivitiesByCategory(progressDialog, this, body)
    }

    override fun onWishlistClick(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder = binding.destinationRecycler.findViewHolderForAdapterPosition(position)
                as? SelectedDestinationAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

    }

    override fun onWishlistClicked(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder = binding.categoriesRecycler.findViewHolderForAdapterPosition(position)
                as? ExploreResultAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

    }

    private fun addToWishlistApi(id: Int?) {
        val body = AddWishlistBody(activity_id = id.toString())
        addWishlistViewModel.addToWishListApi(progressDialog, this, body)
    }

}