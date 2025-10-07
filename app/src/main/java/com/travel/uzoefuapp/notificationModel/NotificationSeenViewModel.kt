package com.travel.uzoefuapp.notificationModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.travel.uzoefuapp.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationSeenViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {

}