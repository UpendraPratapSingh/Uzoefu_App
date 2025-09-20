package com.travel.uzoefuapp.imageUpdateModel

import CustomProgressDialog
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class ImageUpdateViewModel @Inject constructor(
    application: Application,
    private val repository: CommonRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mCategoryResponse = MutableLiveData<Event<ImageUpdateResponse>>()

    fun imageUpdateApi(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        imagePart: MultipartBody.Part
    ) = viewModelScope.launch {
        try {
            progressDialog.start("")
            progressIndicator.value = true

            val response = repository.imageUpdate(imagePart)
            mCategoryResponse.value = Event(response)

        } catch (e: Exception) {
            errorResponse.value = e
        } finally {
            progressIndicator.value = false
            progressDialog.stop()
        }
    }
}
