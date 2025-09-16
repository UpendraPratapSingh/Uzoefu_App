package com.travel.uzoefuapp.loginModel

import CustomProgressDialog
import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.utils.Event
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: CommonRepository
) :
    AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mRegisterResponse = MutableLiveData<Event<LoginResponse>>()

    fun userLoginApi(progressDialog: CustomProgressDialog, activity: Activity, body: LoginBody) =
        viewModelScope.launch {
            getSignUp(progressDialog, activity, body)
        }

    private suspend fun getSignUp(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: LoginBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.loginApi(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<LoginResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: LoginResponse) {
                    progressIndicator.value = false
                    mRegisterResponse.value = Event(value)
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