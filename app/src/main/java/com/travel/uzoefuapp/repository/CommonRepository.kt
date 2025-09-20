package com.travel.uzoefuapp.repository

import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistResponse
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistBody
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistResponse
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import com.travel.uzoefuapp.forgetPasswordModel.ForgotPasswordBody
import com.travel.uzoefuapp.forgetPasswordModel.ForgotPasswordResponse
import com.travel.uzoefuapp.forgetPasswordModel.OtpVerificationBody
import com.travel.uzoefuapp.forgetPasswordModel.OtpVerificationResponse
import com.travel.uzoefuapp.forgetPasswordModel.ResetPasswordBody
import com.travel.uzoefuapp.forgetPasswordModel.ResetPasswordResponse
import com.travel.uzoefuapp.getProfileModel.GetProfileResponse
import com.travel.uzoefuapp.imageUpdateModel.ImageUpdateResponse
import com.travel.uzoefuapp.loginModel.LoginBody
import com.travel.uzoefuapp.loginModel.LoginResponse
import com.travel.uzoefuapp.logoutModel.LogoutResponse
import com.travel.uzoefuapp.services.ApiServices
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileBody
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
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

    fun addToWishlist(body: AddWishlistBody): Observable<AddWishlistResponse> {
        return services.addToWishlist(Uzoefu.encryptedPrefs.bearerToken, body)
    }

    fun getWishlist(): Observable<GetWishlistResponse> {
        return services.getWishlistApi(Uzoefu.encryptedPrefs.bearerToken)
    }

    fun getProfile(): Observable<GetProfileResponse> {
        return services.getProfile(Uzoefu.encryptedPrefs.bearerToken)
    }

    suspend fun imageUpdate(image: MultipartBody.Part): ImageUpdateResponse {
        return services.updateProfileImage(image, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun updateProfile(body: UpdateProfileBody): Observable<UpdateProfileResponse> {
        return services.updateProfile(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun deleteWishList(body: DeleteWishlistBody): Observable<DeleteWishlistResponse> {
        return services.wishListDelete(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun forgotPassword(body: ForgotPasswordBody): Observable<ForgotPasswordResponse> {
        return services.forgotPassword(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun otpVerification(body: OtpVerificationBody): Observable<OtpVerificationResponse>{
        return services.verifyOtp(body)
    }

    fun resetPassword(body: ResetPasswordBody): Observable<ResetPasswordResponse>{
        return services.resetPassword(body)
    }

}