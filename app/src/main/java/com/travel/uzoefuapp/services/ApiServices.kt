package com.travel.uzoefuapp.services

import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistResponse
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import com.travel.uzoefuapp.getProfileModel.GetProfileResponse
import com.travel.uzoefuapp.loginModel.LoginBody
import com.travel.uzoefuapp.loginModel.LoginResponse
import com.travel.uzoefuapp.logoutModel.LogoutResponse
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServices {

    @POST("register")
    fun signUpApi(
        @Body body: SignUpBody,
    ): Observable<SignUpResponse>

    @POST("login")
    fun loginApi(
        @Body body: LoginBody
    ): Observable<LoginResponse>

    @POST("category")
    fun getCategoryApi(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
    ): Observable<CategoryResponse>

    @POST("logout")
    fun logoutApi(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<LogoutResponse>

    @POST("activity/list")
    fun getActivityData(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
        @Body body: ActivityBody
    ): Observable<ActivityResponse>

    @POST("activity/detail")
    fun detailPageApi(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
        @Body body: DetailPageBody
    ): Observable<DetailPageResponse>

    @POST("wishlist")
    fun addToWishlist(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
        @Body body: AddWishlistBody
    ): Observable<AddWishlistResponse>

    @POST("wishlist/data")
    fun getWishlistApi(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<GetWishlistResponse>

    @POST("profile/list")
    fun getProfile(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<GetProfileResponse>

}