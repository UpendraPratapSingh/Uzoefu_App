package com.travel.uzoefuapp.redeemRewardModel

import CustomProgressDialog
import android.app.Activity
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.travel.uzoefuapp.repository.CommonRepository
import com.travel.uzoefuapp.rewardHistoryModel.RewardHistoryResponse
import com.travel.uzoefuapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardRedeemViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val rewardRedeemResponse = MutableLiveData<Event<RewardRedeemResponse>>()

    fun rewardRedeemListApi(
        activity: Activity,
        progressDialog: CustomProgressDialog,
    ) =
        viewModelScope.launch {
            rewardRedeemList(activity, progressDialog)
        }

    private suspend fun rewardRedeemList(
        activity: Activity,
        progressDialog: CustomProgressDialog,
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.rewardListApi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<RewardRedeemResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: RewardRedeemResponse) {
                    progressIndicator.value = false
                    rewardRedeemResponse.value = Event(value)
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