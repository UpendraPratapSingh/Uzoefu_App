package com.travel.uzoefuapp.fragment

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.adapter.DestinationAdapter
import com.travel.uzoefuapp.adapter.DestinationCategoryAdapter
import com.travel.uzoefuapp.adapter.DiscoverAdapter
import com.travel.uzoefuapp.adapter.OnWishlistClickListener
import com.travel.uzoefuapp.branchWishlistModel.BranchWishlistBody
import com.travel.uzoefuapp.branchWishlistModel.BranchWishlistViewModel
import com.travel.uzoefuapp.databinding.FragmentExploreBinding
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationViewModel
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExploreFragment : Fragment(), OnWishlistClickListener {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val discoverDestinationViewModel: DiscoverDestinationViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var discoverList: List<DiscoverDestinationResponse.Datum> = ArrayList()
    private val branchWishlistViewModel: BranchWishlistViewModel by viewModels()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        discoverDestinationApi()
        discoverDestinationObserver()
        branchAddToWishlistObserver()
        notificationCountApi()
        notificationCountObserver()

        binding.menuIcon.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun branchAddToWishlistObserver() {
        branchWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        branchWishlistViewModel.branchWishlistResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

        }
        branchWishlistViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun discoverDestinationObserver() {
        discoverDestinationViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        discoverDestinationViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            discoverList = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (discoverList.isEmpty()) {
                    binding.destinationRecycler.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                } else {
                    binding.noDataText.visibility = View.GONE
                    binding.destinationRecycler.visibility = View.VISIBLE
                    binding.destinationRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                    val categoryAdapter = DestinationAdapter(requireContext(), discoverList, this)
                    binding.destinationRecycler.adapter = categoryAdapter

                    binding.categoriesRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                    binding.categoriesRecycler.adapter =
                        DestinationCategoryAdapter(requireContext(), discoverList)

                }
            }
        }
        discoverDestinationViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun discoverDestinationApi() {
        discoverDestinationViewModel.discoverDestinationApi(progressDialog, requireActivity())

    }

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        notificationCountViewModel.notificationCountResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                binding.notificationBadge.text = data.toString()
            }

        }
        notificationCountViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(requireActivity(), progressDialog)

    }


    override fun onWishlistClicked(product: ActivityResponse.Datum, position: Int) {

    }

    override fun onWishlistDestinationClicked(
        product: DiscoverDestinationResponse.Datum,
        position: Int
    ) {
        product.iswish = !(product.iswish ?: false)

        val viewHolder = binding.destinationRecycler.findViewHolderForAdapterPosition(position)
                as? DestinationAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.iswish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        branchAddToWishlistApi(product.branchId)
    }

    private fun branchAddToWishlistApi(branchId: Int?) {
        val body = BranchWishlistBody(
            branchId = branchId.toString()
        )
        branchWishlistViewModel.addBranchWishlistApi(requireActivity(), progressDialog, body)

    }
}