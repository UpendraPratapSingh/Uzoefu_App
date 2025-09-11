package com.travel.uzoefuapp.repository

import com.travel.uzoefuapp.services.ApiServices
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import io.reactivex.Observable
import javax.inject.Inject

class CommonRepository @Inject constructor(private val services: ApiServices) {

    fun postSignUp(body: SignUpBody): Observable<SignUpResponse> {
        return services.signUpApi(body)
    }
}