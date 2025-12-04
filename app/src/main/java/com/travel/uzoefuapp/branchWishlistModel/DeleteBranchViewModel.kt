package com.travel.uzoefuapp.branchWishlistModel

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
class DeleteBranchViewModel @Inject constructor(
    application: Application,
    private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val userShareRewardResponse = MutableLiveData<Event<DeleteBranchResponse>>()

    fun deleteBranchApi(
        activity: Activity,
        progressDialog: CustomProgressDialog,
        body: DeleteBranchBody
    ) =
        viewModelScope.launch {
            deleteBranch(activity, progressDialog, body)
        }

    private suspend fun deleteBranch(
        activity: Activity,
        progressDialog: CustomProgressDialog,
        body: DeleteBranchBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.deleteBranchWishlist(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<DeleteBranchResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: DeleteBranchResponse) {
                    progressIndicator.value = false
                    userShareRewardResponse.value = Event(value)
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