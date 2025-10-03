package com.travel.uzoefuapp.discoverDestinationModel

import CustomProgressDialog
import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DestinationDetailViewModel @Inject constructor(
    application: Application, private val repository: CommonRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mCategoryResponse = MutableLiveData<Event<DestinationDetailResponse>>()

    fun discoverDestinationDetailApi(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: DestinationDetailBody
    ) =
        viewModelScope.launch {
            discoverDestination(progressDialog, activity, body)
        }

    private suspend fun discoverDestination(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: DestinationDetailBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.destinationDetail(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<DestinationDetailResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: DestinationDetailResponse) {
                    progressIndicator.value = false
                    mCategoryResponse.value = Event(value)
                    progressDialog.stop()
                }

                override fun onError(e: Throwable) {
                    progressIndicator.value = false
                    errorResponse.value = e
                    progressDialog.stop()
                }

                override fun onComplete() {
                    progressIndicator.value = false
                    progressDialog.stop()
                }
            })
    }
}