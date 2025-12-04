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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistViewModel
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.OnDeleteWishListListener
import com.travel.uzoefuapp.adapter.TourAdapter
import com.travel.uzoefuapp.adapter.WishlistAdapter
import com.travel.uzoefuapp.addTripModel.AddTripBody
import com.travel.uzoefuapp.addTripModel.AddTripViewModel
import com.travel.uzoefuapp.addTripModel.GetTripResponse
import com.travel.uzoefuapp.addTripModel.GetTripViewModel
import com.travel.uzoefuapp.databinding.FragmentWishlistActivityBinding
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistBody
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistViewModel
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistActivityFragment : Fragment(), OnDeleteWishListListener {
    private var _binding: FragmentWishlistActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WishlistAdapter
    private var isEditMode = false
    private var wishListId = ""
    private lateinit var recyclerView: RecyclerView
    private val selectedWishlistIds = mutableListOf<String>()
    var data: List<GetWishlistResponse.Datum> = ArrayList()
    private val getWishlistViewModel: GetWishlistViewModel by viewModels()
    private val deleteWishlistViewModel: DeleteWishlistViewModel by viewModels()
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
        _binding = FragmentWishlistActivityBinding.inflate(inflater, container, false)

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
        val body = DeleteWishlistBody(
            wishlistId = selectedWishlistIds
        )
        deleteWishlistViewModel.deleteWishListApi(progressDialog, requireActivity(), body)

    }

    private fun deleteWishListObserver() {
        deleteWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        deleteWishlistViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
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
        getWishlistViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
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
                    adapter = WishlistAdapter(requireContext(), data, this)
                    binding.wishlistRecycler.adapter = adapter
                }
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
        recyclerView = view.findViewById(R.id.recyclerView)
        val createNewTrip = view.findViewById<LinearLayout>(R.id.createNewTrip)

        getTripListApi()

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
                    Toast.makeText(
                        requireContext(),
                        "Trip Created: $title → $destination",
                        Toast.LENGTH_SHORT
                    ).show()
                    addTripApi(title, destination)

                    bottomSheetDialog.dismiss()
                }
            }
            bottomSheetDialog.show()
        }
        bottomSheetDialog.show()
    }

    private fun getTripListApi() {
        getTripViewModel.tripListApi(progressDialog, requireActivity())
    }

    private fun addTripApi(title: String, destination: String) {
        val body = AddTripBody(title = title, destination = destination)
        addTripViewModel.addTripApi(progressDialog, requireActivity(), body)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*    // Agar adapter se poori selectedIds list mil rahi ho (ex: multiple selection)
        override fun onWishlistClicked(selectedIds: List<String>, position: Int) {
            // Pehle purani IDs clear nahi karenge, sirf naye add karenge jo already list me nahi hain
            selectedWishlistIds.addAll(
                selectedIds.map { it.toString() }
                    .filter { !selectedWishlistIds.contains(it) } // duplicates avoid
            )

            Log.d("SelectedIDs", selectedWishlistIds.toString())
        }*/

    override fun onWishlistClicked(selectedIds: List<String>, position: Int) {
        val newSelectedIds = selectedIds.map { it.toString() }

        selectedWishlistIds.retainAll(newSelectedIds)

        selectedWishlistIds.addAll(
            newSelectedIds.filter { !selectedWishlistIds.contains(it) }
        )
        Log.d("SelectedIDs", selectedWishlistIds.toString())
    }
}