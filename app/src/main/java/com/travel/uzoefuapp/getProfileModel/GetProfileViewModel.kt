package com.travel.uzoefuapp.getProfileModel


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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class GetProfileViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application){
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mCategoryResponse = MutableLiveData<Event<GetProfileResponse>>()

    fun getProfileApi(progressDialog: CustomProgressDialog, activity: Activity) =
        viewModelScope.launch {
            getProfile(progressDialog, activity)
        }

    private suspend fun getProfile(progressDialog: CustomProgressDialog, activity: Activity) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.getProfile(

        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<GetProfileResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: GetProfileResponse) {
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