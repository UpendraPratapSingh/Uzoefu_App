package com.travel.uzoefuapp.repository

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
import com.travel.uzoefuapp.overviewModel.OverviewResponse
import com.travel.uzoefuapp.paymentModel.PaymentResponse
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationBody
import com.travel.uzoefuapp.priceCalculationModel.PriceCalculationResponse
import com.travel.uzoefuapp.ratingModel.RatingResponse
import com.travel.uzoefuapp.services.ApiServices
import com.travel.uzoefuapp.signUpModel.SignUpBody
import com.travel.uzoefuapp.signUpModel.SignUpResponse
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileBody
import com.travel.uzoefuapp.updateProfileModel.UpdateProfileResponse
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
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

    fun otpVerification(body: OtpVerificationBody): Observable<OtpVerificationResponse> {
        return services.verifyOtp(body)
    }

    fun resetPassword(body: ResetPasswordBody): Observable<ResetPasswordResponse> {
        return services.resetPassword(body)
    }

    suspend fun submitRatingApi(
        activityId: String,
        rating: String,
        description: String,
        imageFiles: List<File>
    ): Response<RatingResponse> {

        val activityIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), activityId)
        val ratingBody = RequestBody.create("text/plain".toMediaTypeOrNull(), rating)
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)

        val imageParts = imageFiles.mapIndexed { index, file ->
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("images[$index]", file.name, requestFile)
        }

        return services.submitRating(
            Uzoefu.encryptedPrefs.bearerToken,
            activityIdBody,
            ratingBody,
            descriptionBody,
            imageParts
        )
    }

    fun priceCalculation(body: PriceCalculationBody): Observable<PriceCalculationResponse> {
        return services.priceCalculation(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun bookingComplete(body: BookingCompleteBody): Observable<BookingCompleteResponse> {
        return services.bookingComplete(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun bookingDetail(body: BookingDetailBody): Observable<BookingDetailResponse> {
        return services.bookingDetail(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun discoverDestination(): Observable<DiscoverDestinationResponse> {
        return services.discoverDestination(Uzoefu.encryptedPrefs.bearerToken)
    }

    fun destinationDetail(body: DestinationDetailBody): Observable<DestinationDetailResponse> {
        return services.discoverDetail(body, Uzoefu.encryptedPrefs.bearerToken)
    }

    fun overview(): Observable<OverviewResponse> {
        return services.overview(Uzoefu.encryptedPrefs.bearerToken)
    }

    fun addTrip(body: AddTripBody): Observable<AddTripResponse> {
        return services.addTrip(Uzoefu.encryptedPrefs.bearerToken, body)
    }

    fun tripList(): Observable<GetTripResponse>{
        return services.tripList(Uzoefu.encryptedPrefs.bearerToken)
    }

    suspend fun makePayment(
        activityId: String,
        date: String,
        adultCount: String,
        kidsCount: String,
        adultPrice: String,
        kidsPrice: String,
        subtotal: String,
        total: String,
        firstName: String,
        surname: String,
        username: String,
        mobileNumber: String,
        billingAddress: String,
        clientNames: List<MultipartBody.Part>,
        idNumbers: List<MultipartBody.Part>,
        contactNumbers: List<MultipartBody.Part>,
        signInDates: List<MultipartBody.Part>,
        signatureImages: List<MultipartBody.Part>
    ): Response<PaymentResponse> {

        // Single values as RequestBody
        val activityIdBody = activityId.toRequestBody("text/plain".toMediaTypeOrNull())
        val dateBody = date.toRequestBody("text/plain".toMediaTypeOrNull())
        val adultCountBody = adultCount.toRequestBody("text/plain".toMediaTypeOrNull())
        val kidsCountBody = kidsCount.toRequestBody("text/plain".toMediaTypeOrNull())
        val adultPriceBody = adultPrice.toRequestBody("text/plain".toMediaTypeOrNull())
        val kidsPriceBody = kidsPrice.toRequestBody("text/plain".toMediaTypeOrNull())
        val subtotalBody = subtotal.toRequestBody("text/plain".toMediaTypeOrNull())
        val totalBody = total.toRequestBody("text/plain".toMediaTypeOrNull())
        val firstNameBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val surnameBody = surname.toRequestBody("text/plain".toMediaTypeOrNull())
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val mobileNumberBody = mobileNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val billingAddressBody = billingAddress.toRequestBody("text/plain".toMediaTypeOrNull())

        /* // Arrays as List<RequestBody>
         val clientNameBodies = clientNames.map {
             it.toRequestBody("text/plain".toMediaTypeOrNull())
         }

         val idNumberBodies = idNumbers.map {
             it.toRequestBody("text/plain".toMediaTypeOrNull())
         }

         val contactNumberBodies = contactNumbers.map {
             it.toRequestBody("text/plain".toMediaTypeOrNull())
         }

         val signInDateBodies = signInDates.map {
             it.toRequestBody("text/plain".toMediaTypeOrNull())
         }*/


        return services.paymentBooking(
            Uzoefu.encryptedPrefs.bearerToken,
            activityId = activityIdBody,
            date = dateBody,
            adultcount = adultCountBody,
            kidscount = kidsCountBody,
            adultprice = adultPriceBody,
            kidsprice = kidsPriceBody,
            subtotal = subtotalBody,
            total = totalBody,
            firstname = firstNameBody,
            surname = surnameBody,
            username = usernameBody,
            mobile_number = mobileNumberBody,
            billingaddress = billingAddressBody,
            clientNames = clientNames,
            idNumbers = idNumbers,
            contactNumbers = contactNumbers,
            signInDates = signInDates,
            signatureImages = signatureImages  // Already MultipartBody.Part, no conversion needed
        )
    }

    // Extension function to convert String to RequestBody
    private fun String.toRequestBody(toMediaTypeOrNull: MediaType?): RequestBody =
        RequestBody.create("text/plain".toMediaTypeOrNull(), this)

    // Extension function to convert File to MultipartBody.Part
    private fun File.toMultipartPart(fieldName: String): MultipartBody.Part {
        val requestFile = this.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, this.name, requestFile)
    }

}