package com.travel.uzoefuapp.addTripModel

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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetTripViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val getTripResponse = MutableLiveData<Event<GetTripResponse>>()

    fun tripListApi(progressDialog: CustomProgressDialog, activity: Activity) =
        viewModelScope.launch {
            tripList(progressDialog, activity)
        }

    private suspend fun tripList(progressDialog: CustomProgressDialog, activity: Activity) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.tripList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<GetTripResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: GetTripResponse) {
                    progressIndicator.value = false
                    getTripResponse.value = Event(value)
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