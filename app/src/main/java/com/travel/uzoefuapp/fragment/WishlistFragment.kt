package com.travel.uzoefuapp.fragment

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistViewModel
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.WishlistAdapter
import com.travel.uzoefuapp.databinding.FragmentWishlistBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WishlistAdapter
    private var isEditMode = false
    var data: List<GetWishlistResponse.Datum> = ArrayList()
    private val getWishlistViewModel: GetWishlistViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        getWishListApi()
        getWishListObserver()

        binding.menuIcon.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.editText.setOnClickListener {
            val transition = AutoTransition().apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
            TransitionManager.beginDelayedTransition(binding.root, transition)

            isEditMode = true
            adapter.setEditMode(isEditMode)

            binding.editText.visibility = View.GONE
            binding.iconLayout.visibility = View.VISIBLE
        }

        binding.copyIcon.setOnClickListener { openBottomSheetTrip() }

        return binding.root
    }

    private fun getWishListObserver() {
        getWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getWishlistViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            data = response.peekContent().data ?: emptyList()

            if (success == true) {
                binding.wishlistRecycler.layoutManager =
                    GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

                adapter = WishlistAdapter(requireContext(), data)
                binding.wishlistRecycler.adapter = adapter
            }

        }
        getWishlistViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getWishListApi() {
        getWishlistViewModel.getWishlistApi(progressDialog, requireActivity())

    }

    private fun openBottomSheetTrip() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)

        val closeBtn = view.findViewById<ImageView>(R.id.tvCloseBtn)
        val createNewTrip = view.findViewById<LinearLayout>(R.id.createNewTrip)

        closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        createNewTrip.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.bottom_sheet_create_trip, null)
            bottomSheetDialog.setContentView(view)

            val closeBtn = view.findViewById<ImageView>(R.id.tvCloseBtn)
            val etTripTitle = view.findViewById<EditText>(R.id.etTripTitle)
            val etTripDestination = view.findViewById<EditText>(R.id.etTripDestination)
            val btnSaveTrip = view.findViewById<Button>(R.id.btnSaveTrip)

            closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

            btnSaveTrip.setOnClickListener {
                val title = etTripTitle.text.toString().trim()
                val destination = etTripDestination.text.toString().trim()

                if (title.isEmpty() || destination.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(),"Trip Created: $title → $destination", Toast.LENGTH_SHORT).show()
                    bottomSheetDialog.dismiss()
                }
            }
            bottomSheetDialog.show()
        }
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
