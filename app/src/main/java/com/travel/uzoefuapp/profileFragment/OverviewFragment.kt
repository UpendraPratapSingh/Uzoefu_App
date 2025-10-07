package com.travel.uzoefuapp.profileFragment

import CustomProgressDialog
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.LoginActivity
import com.travel.uzoefuapp.activities.TermAndConditionActivity
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.bookingActivities.BookListActivity
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.FragmentOverviewBinding
import com.travel.uzoefuapp.fragment.ProfileFragment
import com.travel.uzoefuapp.getProfileModel.GetProfileViewModel
import com.travel.uzoefuapp.imageUpdateModel.ImageUpdateViewModel
import com.travel.uzoefuapp.logoutModel.LogoutViewModel
import com.travel.uzoefuapp.overviewModel.OverviewViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val CAMERA_PERMISSION_CODE = 101
    private val logoutViewModel: LogoutViewModel by viewModels()
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private val imageUpdateViewModel: ImageUpdateViewModel by viewModels()
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var imageUri: Uri? = null
    private val binding get() = _binding!!
    private val overviewViewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        //Called Observer
        getProfileApi()
        overviewApi()
        getProfileObserver()
        logoutObserver()
        overviewObserver()

        binding.profileImage.setOnClickListener { showImageSourceDialog() }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    val uri = imageUri
                    if (uri != null) {
                        showConfirmationDialog(uri)
                    }
                }
            }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { showConfirmationDialog(it) }
        }

        binding.bookingsCons.setOnClickListener {
            val intent = Intent(requireContext(), BookListActivity::class.java)
            startActivity(intent)
        }

        binding.termAndCondition.setOnClickListener {
            val intent = Intent(requireContext(), TermAndConditionActivity::class.java)
            startActivity(intent)
        }

        binding.logoutAccount.setOnClickListener { openLogoutCustomPopup() }

        binding.wishlistLayout.setOnClickListener { (activity as? DashboardActivity)?.selectWishlistTab() }

        binding.ivEditProfile.setOnClickListener {
            (parentFragment as? ProfileFragment)?.switchToTab(1)
        }

        return binding.root
    }

    private fun overviewObserver() {
        overviewViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        overviewViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data?.overview
            if (success == true) {
                binding.tvWishlist.text = data?.wishlistcount.toString()
                binding.tvBookings.text = data?.bookingcount.toString()
                binding.tvReviews.text = data?.reviewcount.toString()
            }

        }
        overviewViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun overviewApi() {
        overviewViewModel.overviewApi(progressDialog, requireActivity())
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpen()
                    1 -> openGallery()
                }
            }.show()
    }

    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        try {
            val fileName = "${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().cacheDir, fileName)
            file.createNewFile()
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
            imageUri = uri
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Failed to open camera: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showConfirmationDialog(uri: Uri) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_image_confirmation, null)

        val previewImage = dialogView.findViewById<ImageView>(R.id.previewImage)
        previewImage.setImageURI(uri)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            binding.profileImage.setImageURI(uri)
            dialog.dismiss()

            uploadProfileImage(uri)
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun uploadProfileImage(uri: Uri) {
        val imagePart = uriToMultipart(uri)

        val progressDialog = CustomProgressDialog(requireContext())
        imageUpdateViewModel.imageUpdateApi(progressDialog, requireActivity(), imagePart)

        imageUpdateViewModel.mCategoryResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                val success = response.success
                val massage = response.message
                if (success == true) {
                    Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        imageUpdateViewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uriToMultipart(uri: Uri): MultipartBody.Part {
        val file = uriToFile(uri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("profile_", ".jpg", requireContext().cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return tempFile
    }

    private fun getProfileObserver() {
        getProfileViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getProfileViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                binding.tvName.text = "${data?.name.toString()} ${data?.lastname.toString()}"
                binding.tvUsername.text = data?.username.toString()

                val imagePath = "https://mobappssolutions.in/uzoefu/public/uploads/users/"
                val profileImageUrl = if (!data?.profilePhotoPath.isNullOrEmpty()) {
                    "$imagePath/${data?.profilePhotoPath}"
                } else {
                    null
                }

                Glide.with(requireContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .circleCrop()
                    .into(binding.profileImage)
            }

        }
        getProfileViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getProfileApi() {
        getProfileViewModel.getProfileApi(progressDialog, requireActivity())

    }

    private fun logoutObserver() {
        logoutViewModel.progressIndicator.observe(viewLifecycleOwner) {
        }

        logoutViewModel.mRegisterResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().status
            val message = response.peekContent().message

            if (success == true) {
                Uzoefu.encryptedPrefs.bearerToken = ""
                Uzoefu.encryptedPrefs.isNotification = false
                Uzoefu.encryptedPrefs.isFirstTime = false

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
        logoutViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun openLogoutCustomPopup() {
        val dialogView = layoutInflater.inflate(R.layout.logout_popup, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            dialog.dismiss()
            logoutApi()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun logoutApi() {
        logoutViewModel.userLogoutApi(requireActivity())
    }

    private fun openFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.userFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}