package com.travel.uzoefuapp.services

import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistResponse
import com.travel.uzoefuapp.GetWishlistModel.GetWishlistResponse
import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.addTripModel.AddTripBody
import com.travel.uzoefuapp.addTripModel.AddTripResponse
import com.travel.uzoefuapp.addTripModel.GetTripResponse
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteBody
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteResponse
import com.travel.uzoefuapp.bookingCompleteModel.BookingDetailBody
import com.travel.uzoefuapp.bookingCompleteModel.BookingDetailResponse
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistBody
import com.travel.uzoefuapp.deleteWishlistModel.DeleteWishlistResponse
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import com.travel.uzoefuapp.discoverDestinationModel.DestinationDetailBody
import com.travel.uzoefuapp.discoverDestinationModel.DestinationDetailResponse
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse
import com.travel.uzoefuapp.forgetPasswordModel.OtpVerificationBody
import com.travel.uzoefuapp.forgetPasswordModel.ForgotPasswordBody
import com.travel.uzoefuapp.forgetPasswordModel.ForgotPasswordResponse
import com.travel.uzoefuapp.forgetPasswordModel.OtpVerificationResponse
import com.travel.uzoefuapp.forgetPasswordModel.ResetPasswordBody
import com.travel.uzoefuapp.forgetPasswordModel.ResetPasswordResponse
import com.travel.uzoefuapp.getProfileModel.GetProfileResponse
import com.travel.uzoefuapp.imageUpdateModel.ImageUpdateResponse
import com.travel.uzoefuapp.loginModel.LoginBody
import com.travel.uzoefuapp.loginModel.LoginResponse
import com.travel.uzoefuapp.logoutModel.LogoutResponse
import com.travel.uzoefuapp.overviewModel.OverviewResponse
import com.travel.uzoefuapp.paymentModel.PaymentResponse
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationBody
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationResponse
import com.travel.uzoefuapp.ratingModel.RatingResponse
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileBody
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {

    @POST("register")
    fun signUpApi(@Body body: SignUpBody): Observable<SignUpResponse>

    @POST("login")
    fun loginApi(@Body body: LoginBody): Observable<LoginResponse>

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

    @Multipart
    @POST("profile/image/update")
    suspend fun updateProfileImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): ImageUpdateResponse

    @POST("profile/update")
    fun updateProfile(
        @Body body: UpdateProfileBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<UpdateProfileResponse>

    @POST("wishlist/delete")
    fun wishListDelete(
        @Body body: DeleteWishlistBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<DeleteWishlistResponse>

    @POST("send/otp")
    fun forgotPassword(
        @Body body: ForgotPasswordBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<ForgotPasswordResponse>

    @POST("verify/otp")
    fun verifyOtp(
        @Body body: OtpVerificationBody,
    ): Observable<OtpVerificationResponse>

    @POST("reset/password")
    fun resetPassword(
        @Body body: ResetPasswordBody,
    ): Observable<ResetPasswordResponse>

    @Multipart
    @POST("activity/rating")
    suspend fun submitRating(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
        @Part("activity_id") activityId: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("description") description: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<RatingResponse>

    @POST("price/calculation")
    fun priceCalculation(
        @Body body: PriceCalculationBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<PriceCalculationResponse>

    @POST("booking/list")
    fun bookingComplete(
        @Body body: BookingCompleteBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<BookingCompleteResponse>

    @Multipart
    @POST("payment")
    suspend fun paymentBooking(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
        @Part("activity_id") activityId: RequestBody,
        @Part("date") date: RequestBody,
        @Part("adultcount") adultcount: RequestBody,
        @Part("kidscount") kidscount: RequestBody,
        @Part("adultprice") adultprice: RequestBody,
        @Part("kidsprice") kidsprice: RequestBody,
        @Part("subtotal") subtotal: RequestBody,
        @Part("total") total: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("surname") surname: RequestBody,
        @Part("username") username: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("billingaddress") billingaddress: RequestBody,
        @Part clientNames: List<MultipartBody.Part>,
        @Part idNumbers: List<MultipartBody.Part>,
        @Part contactNumbers: List<MultipartBody.Part>,
        @Part signInDates: List<MultipartBody.Part>,
        @Part signatureImages: List<MultipartBody.Part>
    ): Response<PaymentResponse>

    @POST("booking/detail")
    fun bookingDetail(
        @Body body: BookingDetailBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<BookingDetailResponse>

    @POST("discover/destination")
    fun discoverDestination(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<DiscoverDestinationResponse>

    @POST("discover/detail")
    fun discoverDetail(
        @Body body: DestinationDetailBody,
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<DestinationDetailResponse>

    @POST("overview")
    fun overview(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<OverviewResponse>

    @POST("add/trip")
    fun addTrip(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken,
        @Body body: AddTripBody
    ): Observable<AddTripResponse>

    @POST("trip/list")
    fun tripList(
        @Header("Authorization") token: String = Uzoefu.encryptedPrefs.bearerToken
    ): Observable<GetTripResponse>

}