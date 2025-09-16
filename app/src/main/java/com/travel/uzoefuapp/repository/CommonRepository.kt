package com.travel.uzoefuapp.repository

import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import com.travel.uzoefuapp.loginModel.LoginBody
import com.travel.uzoefuapp.loginModel.LoginResponse
import com.travel.uzoefuapp.logoutModel.LogoutResponse
import com.travel.uzoefuapp.services.ApiServices
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import io.reactivex.Observable
import javax.inject.Inject

class CommonRepository @Inject constructor(private val services: ApiServices) {

    fun postSignUp(body: SignUpBody): Observable<SignUpResponse> {
        return services.signUpApi(body)
    }

    fun loginApi(body: LoginBody): Observable<LoginResponse> {
        return services.loginApi(body)
    }

    fun categoryList(): Observable<CategoryResponse> {
        return services.getCategoryApi(Uzoefu.encryptedPrefs.bearerToken)
    }

    fun logoutApi(): Observable<LogoutResponse> {
        return services.logoutApi(Uzoefu.encryptedPrefs.bearerToken)
    }

    fun getActivity(body: ActivityBody): Observable<ActivityResponse> {
        return services.getActivityData(Uzoefu.encryptedPrefs.bearerToken, body)
    }

    fun detailPage(body: DetailPageBody): Observable<DetailPageResponse> {
        return services.detailPageApi(Uzoefu.encryptedPrefs.bearerToken, body)
    }

}