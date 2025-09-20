package com.travel.uzoefuapp.profileFragment

import CustomProgressDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.FavouriteAdapter
import com.travel.uzoefuapp.adapter.OnCategoryClickListener
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.categoryModel.CategoryViewModel
import com.travel.uzoefuapp.databinding.FragmentProfileDetailBinding
import com.travel.uzoefuapp.getProfileModel.GetProfileViewModel
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileBody
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ProfileDetailFragment : Fragment(), OnCategoryClickListener {
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private val updateProfileViewModel: UpdateProfileViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!
    var data: List<CategoryResponse.Datum> = ArrayList()
    var categoriesId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)

        getProfileApi()
        getProfileObserver()
        updateUserProfile()
        getCategoryApi()
        getCategoryObserver()

        binding.saveButton.setOnClickListener {
            validation()
        }

        binding.dateOfBirth.setOnClickListener {
            showDatePicker()
        }

        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate = sdf.format(calendar.time)

                binding.dateOfBirth.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }


    private fun getCategoryObserver() {
        categoryViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }

        categoryViewModel.mCategoryResponse.observe(viewLifecycleOwner) { event ->
            val content = event.peekContent()
            val success = content.success
            val message = content.message
            data = content.data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.categoriesRecyclerView.visibility = View.GONE
                } else {
                    binding.categoriesRecyclerView.visibility = View.VISIBLE
                    binding.categoriesRecyclerView.layoutManager =
                        GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = CategoryAdapter(requireContext(), data, this)
                    binding.categoriesRecyclerView.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    message ?: "Failed to load categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        categoryViewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            ErrorUtil.handlerGeneralError(requireContext(), error)
        }
    }

    private fun getCategoryApi() {
        categoryViewModel.getCategory(progressDialog, requireActivity())

    }

    private fun updateUserProfile() {
        updateProfileViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        updateProfileViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                getProfileApi()

            }

        }
        updateProfileViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun inputValidation(
        firstNameValid: String,
        lastNameValid: String,
        isUserNameValid: String,
        emailValid: String,
        dateOfBirth: String,
        mobileValid: String,
        cityValid: String,
    ): Boolean {
        var isValid = true

        if (firstNameValid.isEmpty()) {
            // binding.firstName.error = "First Name can't be empty"
            Toast.makeText(requireContext(), "First Name is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (lastNameValid.isEmpty()) {
            // binding.lastName.error = "Last Name can't be empty"
            Toast.makeText(requireContext(), "Last Name is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (isUserNameValid.isEmpty()) {
            // binding.lastName.error = "Last Name can't be empty"
            Toast.makeText(requireContext(), "User Name is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (emailValid.isEmpty()) {
            //  binding.email.error = "Email can't be empty"
            Toast.makeText(requireContext(), "Email is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (dateOfBirth.isEmpty()) {
            //  binding.email.error = "Email can't be empty"
            Toast.makeText(requireContext(), "Date of Birth is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (mobileValid.isEmpty()) {
            // binding.mobileNumber.error = "Mobile Number can't be empty"
            Toast.makeText(requireContext(), "Mobile Number is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (cityValid.isEmpty()) {
            // binding.cityName.error = "City can't be empty"
            Toast.makeText(requireContext(), "City is empty!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid

    }

    private fun validation() {
        val isFirstNameValid = binding.firstName.text.toString().trim()
        val isLastNameValid = binding.lastName.text.toString().trim()
        val isUserNameValid = binding.etUsername.text.toString().trim()
        val isEmailValid = binding.email.text.toString().trim()
        val dateOfBirth = binding.dateOfBirth.text.toString().trim()
        val isMobileValid = binding.mobileNumber.text.toString().trim()
        val isCityValid = binding.cityName.text.toString().trim()
        val isDistanceValid = binding.spinnerDistanceRange.text.toString().trim()

        if (inputValidation(
                isFirstNameValid,
                isLastNameValid,
                isUserNameValid,
                isEmailValid,
                dateOfBirth,
                isMobileValid,
                isCityValid,
            )
        ) {
            updateProfileApi(
                isFirstNameValid,
                isLastNameValid,
                isUserNameValid,
                isEmailValid,
                dateOfBirth,
                isMobileValid,
                isCityValid,
                isDistanceValid
            )
        }
    }

    private fun updateProfileApi(
        isFirstNameValid: String,
        isLastNameValid: String,
        isUserNameValid: String,
        isEmailValid: String,
        dateOfBirth: String,
        isMobileValid: String,
        isCityValid: String,
        isDistanceValid: String,
    ) {
        val body = UpdateProfileBody(
            first_name = isFirstNameValid,
            surname = isLastNameValid,
            username = isUserNameValid,
            email = isEmailValid,
            dateofbirth = dateOfBirth,
            mobile = isMobileValid,
            city = isCityValid,
            distance = isDistanceValid,
            categoryIds = listOf(categoriesId)

        )
        updateProfileViewModel.updateProfileApi(progressDialog, requireActivity(), body)

    }

    private fun getProfileObserver() {
        getProfileViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getProfileViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data

            if (success == true) {
                binding.firstName.setText(data?.name.toString())
                binding.lastName.setText(data?.lastname.toString())
                binding.dateOfBirth.setText(data?.dateofbirth.toString())
                binding.email.setText(data?.email.toString())
                binding.mobileNumber.setText(data?.mobile.toString())
                binding.cityName.setText(data?.city.toString())
                binding.spinnerDistanceRange.setText(data?.distance.toString())
            }
        }
        getProfileViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getProfileApi() {
        getProfileViewModel.getProfileApi(progressDialog, requireActivity())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val distanceRanges = listOf("1 km", "5 km", "10 km", "20 km", "50 km")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            distanceRanges
        )

        binding.spinnerDistanceRange.setAdapter(adapter)

        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = FavouriteAdapter(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCategoryClick(categoryId: String, categoryName: String) {
        categoriesId = categoryId

    }
}
