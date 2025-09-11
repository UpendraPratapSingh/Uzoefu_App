package com.travel.uzoefuapp.signUpModel

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
class SignUpViewModel @Inject constructor(
    application: Application,
    private val repository: CommonRepository
) :
    AndroidViewModel(application) {
        val progressIndicator = MutableLiveData<Boolean>()
        val errorResponse = MutableLiveData<Throwable>()
        val mRegisterResponse = MutableLiveData<Event<SignUpResponse>>()

    fun signUpUser(activity: Activity, body: SignUpBody) =
        viewModelScope.launch {
            getSignUp(activity, body)
        }

    private suspend fun getSignUp(
        activity: Activity,
        body: SignUpBody
    ) {

        progressIndicator.value = true
        repository.postSignUp(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<SignUpResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: SignUpResponse) {
                    progressIndicator.value = false
                    mRegisterResponse.value = Event(value)
                }

                override fun onError(e: Throwable) {
                    progressIndicator.value = false
                    errorResponse.value = e
                }

                override fun onComplete() {
                    progressIndicator.value = false
                }
            })
    }
}