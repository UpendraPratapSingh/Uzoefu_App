package com.travel.uzoefuapp.deleteWishlistModel

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
class DeleteWishlistViewModel @Inject constructor(
    application: Application, private val repository: CommonRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mCategoryResponse = MutableLiveData<Event<DeleteWishlistResponse>>()

    fun deleteWishListApi(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: DeleteWishlistBody
    ) =
        viewModelScope.launch {
            deleteWishList(progressDialog, activity, body)
        }

    private suspend fun deleteWishList(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: DeleteWishlistBody
    ) {
        progressDialog.start("")
        progressIndicator.value = true
        repository.deleteWishList(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<DeleteWishlistResponse>() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onNext(value: DeleteWishlistResponse) {
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