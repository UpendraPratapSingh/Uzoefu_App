package com.travel.uzoefuapp.notificationModel

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
class NotificationDeleteViewModel @Inject constructor(
    application: Application, private val repository: CommonRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val notificationDeleteResponse = MutableLiveData<Event<NotificationDeleteResponse>>()

    fun notificationDeleteApi(
        activity: Activity,
        progressDialog: CustomProgressDialog,
        body: NotificationDeleteBody
    ) =
        viewModelScope.launch {
            notificationDelete(activity, progressDialog, body)
        }

    private suspend fun notificationDelete(
        activity: Activity,
        progressDialog: CustomProgressDialog,
        body: NotificationDeleteBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.notificationDelete(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<NotificationDeleteResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: NotificationDeleteResponse) {
                    progressIndicator.value = false
                    notificationDeleteResponse.value = Event(value)
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