package com.travel.uzoefuapp.productFragment

import CustomProgressDialog
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.PhotoAdapter
import com.travel.uzoefuapp.adapter.Review
import com.travel.uzoefuapp.adapter.ReviewAdapter
import com.travel.uzoefuapp.databinding.FragmentProductReviewBinding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductReviewFragment : Fragment() {
    private var _binding: FragmentProductReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var adapter: PhotoAdapter
    private val photos = mutableListOf<Uri>()
    private val PICK_IMAGES = 1001
    private var categoryId: Int? = null
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }


    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        if (photos.size >= 10) break
                        val uri = data.clipData!!.getItemAt(i).uri
                        photos.add(uri)
                    }
                } else if (data?.data != null) {
                    if (photos.size < 10) photos.add(data.data!!)
                }
                adapter.notifyDataSetChanged()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductReviewBinding.inflate(inflater, container, false)
        categoryId = arguments?.getInt("categoryId")
        binding.writeReviewBtn.setOnClickListener { showReviewBottomSheet(requireContext()) }

        //getDetailObserver()
        //categoryId?.let { getDetailApi(it) }

        val reviews = listOf(
            Review(
                userName = "Rahul Sharma",
                timeAgo = "2 days ago",
                rating = 4.5f,
                reviewText = "Very good product, value for money!",
                userImage = R.drawable.balloon,
                images = listOf(R.drawable.product, R.drawable.birds)
            ),
            Review(
                userName = "Priya Verma",
                timeAgo = "1 week ago",
                rating = 5f,
                reviewText = "Awesome quality and fast delivery.",
                userImage = R.drawable.birds,
                images = listOf(R.drawable.product)
            )
        )

        reviewAdapter = ReviewAdapter(reviews)
        binding.recyclerReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReviews.adapter = reviewAdapter

        return binding.root
    }

    private fun showReviewBottomSheet(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(
            R.layout.layout_review_bottom_sheet,
            null
        )

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val addPhotosBtn = view.findViewById<Button>(R.id.btnAddPhotos)
        val experienceEdit = view.findViewById<EditText>(R.id.etExperience)
        val reviewRecyclerView = view.findViewById<RecyclerView>(R.id.review_photo_recycler_view)
        val postBtn = view.findViewById<Button>(R.id.btnPost)
        val closeBtn = view.findViewById<ImageView>(R.id.ivClose)

        closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        adapter = PhotoAdapter(photos) { position ->
            photos.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        reviewRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        reviewRecyclerView.adapter = adapter

        addPhotosBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            pickImagesLauncher.launch(intent)
        }

        experienceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postBtn.isEnabled = s.toString().trim().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        postBtn.setOnClickListener {
            val rating = ratingBar.rating
            val experience = experienceEdit.text.toString().trim()
            Toast.makeText(context, "Posted: $rating stars, $experience", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun getDetailObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data?.ActivityRating()

            if (success == true) {
/*                binding.tvDescription.text = data?.description.toString()
                binding.highlights.text = data?.highlights?.joinToString("\n") { "• $it" } ?: ""
                binding.tvLocation.text = "${data?.address.toString()} , ${data3?.town.toString()}"*/

            }
        }
        detailPageViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getDetailApi(categoryId: Int) {
        val body = DetailPageBody(
            activity_id = categoryId.toString()
        )
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)

    }

}