package com.travel.uzoefuapp.paymentModel

import CustomProgressDialog
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    application: Application, private val repository: CommonRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val paymentResponse = MutableLiveData<Event<PaymentResponse>>()

    fun ratingApi(
        progressDialog: CustomProgressDialog,
        activityId: String,
        date: String,
        times: String,
        adultCount: String,
        kidsCount: String,
        adultPrice: String,
        kidsPrice: String,
        subtotal: String,
        total: String,
        firstName: String,
        surname: String,
        username: String,
        mobileNumber: String,
        billingAddress: String,
        clientNames: List<MultipartBody.Part>,
        idNumbers: List<MultipartBody.Part>,
        contactNumbers: List<MultipartBody.Part>,
        signInDates: List<MultipartBody.Part>,
        signatureImages: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            try {
                progressDialog.start("")
                progressIndicator.value = true

                val response = repository.makePayment(
                    activityId,
                    date,
                    times,
                    adultCount,
                    kidsCount,
                    adultPrice,
                    kidsPrice,
                    subtotal,
                    total,
                    firstName,
                    surname,
                    username,
                    mobileNumber,
                    billingAddress,
                    clientNames,
                    idNumbers,
                    contactNumbers,
                    signInDates,
                    signatureImages
                )

                progressIndicator.value = false
                if (response.isSuccessful && response.body() != null) {
                    paymentResponse.value = Event(response.body()!!)  // ✅ FIXED
                } else {
                    errorResponse.value = Throwable(response.message())
                }
            } catch (e: Throwable) {
                progressIndicator.value = false
                errorResponse.value = e
            } finally {
                progressDialog.stop()
            }
        }
    }
}