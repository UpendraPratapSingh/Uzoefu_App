package com.travel.uzoefuapp.filterActivityModel

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
class FilterActivityViewModel @Inject constructor(
    application: Application, private val repository: CommonRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val filterActivityResponse = MutableLiveData<Event<FilterActivityResponse>>()

    fun filterActivityBody(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: FilterActivityBody
    ) =
        viewModelScope.launch {
            filterActivity(progressDialog, activity, body)
        }

    private suspend fun filterActivity(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: FilterActivityBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.filterActivity(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<FilterActivityResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: FilterActivityResponse) {
                    progressIndicator.value = false
                    filterActivityResponse.value = Event(value)
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