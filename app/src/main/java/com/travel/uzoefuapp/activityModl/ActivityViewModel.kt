package com.travel.uzoefuapp.activityModl

import CustomProgressDialog
import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    application: Application,
    private val repository: CommonRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()

    // Separate LiveData for all activities and category filtered activities
    val allActivitiesResponse = MutableLiveData<Event<ActivityResponse>>()
    val categoryActivitiesResponse = MutableLiveData<Event<ActivityResponse>>()

    // API call for all activities (without categoryId)
    fun getAllActivities(progressDialog: CustomProgressDialog , activity: Activity, body: ActivityBody) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.getActivity(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<ActivityResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: ActivityResponse) {
                    progressIndicator.value = false
                    allActivitiesResponse.value = Event(value)
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

    // API call for activities by category
    fun getActivitiesByCategory(progressDialog: CustomProgressDialog ,activity: Activity, body: ActivityBody) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.getActivity(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<ActivityResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: ActivityResponse) {
                    progressIndicator.value = false
                    categoryActivitiesResponse.value = Event(value)
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
