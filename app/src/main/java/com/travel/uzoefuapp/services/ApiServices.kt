package com.travel.uzoefuapp.services

import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServices {

    @POST("register")
    fun signUpApi(
        @Body body: SignUpBody,
    ): Observable<SignUpResponse>

}