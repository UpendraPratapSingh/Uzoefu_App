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
class BranchWishlistViewModel @Inject constructor(
    application: Application, private val repository: CommonRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val branchWishlistResponse = MutableLiveData<Event<BranchWishlistResponse>>()

    fun addBranchWishlistApi(
        activity: Activity,
        progressDialog: CustomProgressDialog,
        body: BranchWishlistBody
    ) =
        viewModelScope.launch {
            addBranchWishlist(activity, progressDialog, body)
        }

    private suspend fun addBranchWishlist(
        activity: Activity,
        progressDialog: CustomProgressDialog,
        body: BranchWishlistBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.addBranchWishlist(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<BranchWishlistResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: BranchWishlistResponse) {
                    progressIndicator.value = false
                    branchWishlistResponse.value = Event(value)
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