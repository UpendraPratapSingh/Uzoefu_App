package com.travel.uzoefuapp.wishlistFragment

import CustomProgressDialog
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.adapter.BranchWishlistAdapter
import com.travel.uzoefuapp.adapter.OnDeleteWishListListener
import com.travel.uzoefuapp.adapter.TourAdapter
import com.travel.uzoefuapp.addTripModel.AddTripViewModel
import com.travel.uzoefuapp.addTripModel.GetTripResponse
import com.travel.uzoefuapp.addTripModel.GetTripViewModel
import com.travel.uzoefuapp.branchWishlistModel.DeleteBranchBody
import com.travel.uzoefuapp.branchWishlistModel.DeleteBranchViewModel
import com.travel.uzoefuapp.branchWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.branchWishlistModel.GetWishlistViewModel
import com.travel.uzoefuapp.databinding.FragmentWishlistBranchBinding
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistBody
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistViewModel
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WishlistBranchFragment : Fragment(), OnDeleteWishListListener {
    private var _binding: FragmentWishlistBranchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BranchWishlistAdapter
    private var isEditMode = false
    private var wishListId = ""
    private lateinit var recyclerView: RecyclerView
    private val selectedWishlistIds = mutableListOf<String>()
    var data: List<GetWishlistResponse.Datum> = ArrayList()
    private val getWishlistViewModel: GetWishlistViewModel by viewModels()
    private val deleteWishlistViewModel: DeleteBranchViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private val addTripViewModel: AddTripViewModel by viewModels()
    private val getTripViewModel: GetTripViewModel by viewModels()
    private var getList: List<GetTripResponse.Datum> = ArrayList()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistBranchBinding.inflate(inflater, container, false)

        getWishListApi()
        getWishListObserver()
        deleteWishListObserver()
        addTripObserver()
        getTripListObserver()

        binding.deleteIcon.setOnClickListener {
            if (selectedWishlistIds.isNotEmpty()) {
                deleteWishListApi()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select an item to delete",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

        return binding.root
    }

    private fun getTripListObserver() {
        getTripViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getTripViewModel.getTripResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            getList = response.peekContent().data ?: emptyList()
            if (success == true) {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val adapter = TourAdapter(getList)
                recyclerView.adapter = adapter
            }
        }
        getTripViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun addTripObserver() {
        addTripViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        addTripViewModel.addTripResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                getWishListApi()
            }
        }
        addTripViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun deleteWishListApi() {
        val body = DeleteBranchBody(
            wishlist_id = selectedWishlistIds
        )
        deleteWishlistViewModel.deleteBranchApi(requireActivity(), progressDialog, body)

    }

    private fun deleteWishListObserver() {
        deleteWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        deleteWishlistViewModel.userShareRewardResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                getWishListApi()
            }
        }
        deleteWishlistViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getWishListObserver() {
        getWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getWishlistViewModel.userShareRewardResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().status
            data = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.editText.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                    binding.iconLayout.visibility = View.GONE
                    binding.wishlistRecycler.visibility = View.GONE
                } else {
                    binding.iconLayout.visibility = View.GONE
                    binding.noDataText.visibility = View.GONE
                    binding.editText.visibility = View.VISIBLE
                    binding.wishlistRecycler.visibility = View.VISIBLE
                    binding.wishlistRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
                    adapter = BranchWishlistAdapter(requireContext(), data, this)
                    binding.wishlistRecycler.adapter = adapter
                }
            }
        }
        getWishlistViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getWishListApi() {
        getWishlistViewModel.branchWishlist(requireActivity(), progressDialog)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWishlistClicked(selectedIds: List<String>, position: Int) {
        val newSelectedIds = selectedIds.map { it.toString() }

        selectedWishlistIds.retainAll(newSelectedIds)

        selectedWishlistIds.addAll(
            newSelectedIds.filter { !selectedWishlistIds.contains(it) }
        )
        Log.d("SelectedIDs", selectedWishlistIds.toString())
    }
}