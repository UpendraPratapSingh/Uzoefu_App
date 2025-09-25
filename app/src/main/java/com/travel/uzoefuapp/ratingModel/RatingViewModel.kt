package com.travel.uzoefuapp.ratingModel

import CustomProgressDialog
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val ratingResponse = MutableLiveData<Event<RatingResponse>>()

    fun ratingApi(
        progressDialog: CustomProgressDialog,
        activityId: String,
        rating: String,
        description: String,
        imageFiles: List<File>
    ) {
        viewModelScope.launch {
            try {
                progressDialog.start("")
                progressIndicator.value = true

                val response = repository.submitRatingApi(activityId, rating, description, imageFiles)

                progressIndicator.value = false
                if (response.isSuccessful && response.body() != null) {
                    ratingResponse.value = Event(response.body()!!)
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