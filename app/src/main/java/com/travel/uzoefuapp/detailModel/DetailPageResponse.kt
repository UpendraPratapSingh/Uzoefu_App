package com.travel.uzoefuapp.detailModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.travel.uzoefuapp.detailModel.DetailPageResponse.Data.Datum.User
import java.io.Serializable


class DetailPageResponse : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("image_path")
    @Expose
    var imagePath: String? = null

    @SerializedName("next_page_url")
    @Expose
    var nextPageUrl: String? = null

    @SerializedName("prev_page_url")
    @Expose
    var prevPageUrl: String? = null

    @SerializedName("current_page")
    @Expose
    var currentPage: Int? = null

    @SerializedName("last_page")
    @Expose
    var lastPage: Int? = null

    inner class Data {
        @SerializedName("activity")
        @Expose
        var activity: Activity? = null

        @SerializedName("description")
        @Expose
        var description: Description? = null

        @SerializedName("price")
        @Expose
        var price: Price? = null

        @SerializedName("hours")
        @Expose
        var hours: Hours? = null

        @SerializedName("amenities")
        @Expose
        var amenities: List<Amenity>? = null

        @SerializedName("images")
        @Expose
        var images: List<Image>? = null

        @SerializedName("payment")
        @Expose
        var payment: Payment? = null

        @SerializedName("faqs")
        @Expose
        var faqs: List<Faq>? = null

        @SerializedName("terms")
        @Expose
        var terms: Terms? = null

        @SerializedName("indemnity")
        @Expose
        var indemnity: Indemnity? = null

        @SerializedName("activity_rating")
        @Expose
        var activityRating: List<ActivityRating>? = null

        @SerializedName("rating_count")
        @Expose
        var ratingCount: Int? = null

        @SerializedName("iswish")
        @Expose
        var iswish: Boolean? = null

        @SerializedName("today_hours")
        @Expose
        var todayHours: String? = null

        inner class Amenity {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("amenity_id")
            @Expose
            var amenityId: Int? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null

            @SerializedName("amenity")
            @Expose
            var amenity: Amenity__1? = null

            inner class Amenity__1 {
                @SerializedName("id")
                @Expose
                var id: Int? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("status")
                @Expose
                var status: Int? = null

                @SerializedName("created_at")
                @Expose
                var createdAt: String? = null

                @SerializedName("updated_at")
                @Expose
                var updatedAt: String? = null
            }
        }

        inner class ActivityRating {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("rating")
            @Expose
            var rating: Int? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("images")
            @Expose
            var images: List<String>? = null

            @SerializedName("times")
            @Expose
            var times: String? = null

            @SerializedName("user")
            @Expose
            var user: User? = null
        }

        inner class Faq {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("question")
            @Expose
            var question: String? = null

            @SerializedName("answer")
            @Expose
            var answer: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        inner class Datum {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("rating")
            @Expose
            var rating: Int? = null

            @SerializedName("images")
            @Expose
            var images: List<String>? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null

            @SerializedName("user")
            @Expose
            var user: User? = null

            inner class User {
                @SerializedName("user_id")
                @Expose
                var userId: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("image")
                @Expose
                var image: String? = null

            }
        }

        inner class Price {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("currency")
            @Expose
            var currency: String? = null

            @SerializedName("unit_measure")
            @Expose
            var unitMeasure: String? = null

            @SerializedName("effective_date")
            @Expose
            var effectiveDate: String? = null

            @SerializedName("adult_base")
            @Expose
            var adultBase: String? = null

            @SerializedName("children_base")
            @Expose
            var childrenBase: String? = null

            @SerializedName("senior_citizens")
            @Expose
            var seniorCitizens: String? = null

            @SerializedName("student")
            @Expose
            var student: String? = null

            @SerializedName("group_price")
            @Expose
            var groupPrice: String? = null

            @SerializedName("discount_type")
            @Expose
            var discountType: String? = null

            @SerializedName("input_value")
            @Expose
            var inputValue: String? = null

            @SerializedName("effective_date_discount")
            @Expose
            var effectiveDateDiscount: String? = null

            @SerializedName("refund_policy")
            @Expose
            var refundPolicy: String? = null

            @SerializedName("cancellation_policy")
            @Expose
            var cancellationPolicy: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        inner class Hours {
            @SerializedName("mon_from")
            @Expose
            var monFrom: String? = null

            @SerializedName("mon_to")
            @Expose
            var monTo: String? = null

            @SerializedName("tue_from")
            @Expose
            var tueFrom: String? = null

            @SerializedName("tue_to")
            @Expose
            var tueTo: String? = null

            @SerializedName("wed_from")
            @Expose
            var wedFrom: String? = null

            @SerializedName("wed_to")
            @Expose
            var wedTo: String? = null

            @SerializedName("thu_from")
            @Expose
            var thuFrom: String? = null

            @SerializedName("thu_to")
            @Expose
            var thuTo: String? = null

            @SerializedName("fri_from")
            @Expose
            var friFrom: String? = null

            @SerializedName("fri_to")
            @Expose
            var friTo: String? = null

            @SerializedName("sat_from")
            @Expose
            var satFrom: String? = null

            @SerializedName("sat_to")
            @Expose
            var satTo: String? = null

            @SerializedName("sun_from")
            @Expose
            var sunFrom: String? = null

            @SerializedName("sun_to")
            @Expose
            var sunTo: String? = null

            @SerializedName("public_mon_from")
            @Expose
            var publicMonFrom: String? = null

            @SerializedName("public_mon_to")
            @Expose
            var publicMonTo: String? = null
        }

        inner class Payment {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("visa_card")
            @Expose
            var visaCard: Int? = null

            @SerializedName("eft")
            @Expose
            var eft: Int? = null

            @SerializedName("bank_id")
            @Expose
            var bankId: Int? = null

            @SerializedName("bank_branch_id")
            @Expose
            var bankBranchId: Int? = null

            @SerializedName("account_holder_name")
            @Expose
            var accountHolderName: String? = null

            @SerializedName("account_type")
            @Expose
            var accountType: String? = null

            @SerializedName("account_number")
            @Expose
            var accountNumber: String? = null

            @SerializedName("branch_code")
            @Expose
            var branchCode: String? = null

            @SerializedName("swift_code")
            @Expose
            var swiftCode: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null

            @SerializedName("bank")
            @Expose
            var bank: Bank? = null

            @SerializedName("bankbranch")
            @Expose
            var bankbranch: Bankbranch? = null


            inner class Bank {
                @SerializedName("id")
                @Expose
                var id: Int? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("created_at")
                @Expose
                var createdAt: String? = null

                @SerializedName("updated_at")
                @Expose
                var updatedAt: String? = null
            }

            inner class Bankbranch {
                @SerializedName("id")
                @Expose
                var id: Int? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("bank_id")
                @Expose
                var bankId: Int? = null

                @SerializedName("created_at")
                @Expose
                var createdAt: String? = null

                @SerializedName("updated_at")
                @Expose
                var updatedAt: String? = null
            }
        }

        inner class Terms {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("disclaimer")
            @Expose
            var disclaimer: Any? = null

            @SerializedName("terms_and_conditions")
            @Expose
            var termsAndConditions: Any? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        inner class Indemnity {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("require_indemnity")
            @Expose
            var requireIndemnity: Int? = null

            @SerializedName("signing_detail")
            @Expose
            var signingDetail: String? = null

            @SerializedName("agreement")
            @Expose
            var agreement: String? = null

            @SerializedName("waiver_and_indemnity")
            @Expose
            var waiverAndIndemnity: String? = null

            @SerializedName("declaration")
            @Expose
            var declaration: String? = null

            @SerializedName("acknowledgement")
            @Expose
            var acknowledgement: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        inner class Image {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("image")
            @Expose
            var image: String? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        inner class Description {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("activity_id")
            @Expose
            var activityId: Int? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("highlights")
            @Expose
            var highlights: List<String>? = null

            @SerializedName("deleted_at")
            @Expose
            var deletedAt: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        inner class Activity {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("activity_name")
            @Expose
            var activityName: String? = null

            @SerializedName("branch_id")
            @Expose
            var branchId: Int? = null

            @SerializedName("user_id")
            @Expose
            var userId: Int? = null

            @SerializedName("contact_name")
            @Expose
            var contactName: String? = null

            @SerializedName("last_name")
            @Expose
            var lastName: String? = null

            @SerializedName("user_email_address")
            @Expose
            var userEmailAddress: String? = null

            @SerializedName("google_email")
            @Expose
            var googleEmail: String? = null

            @SerializedName("contact_number")
            @Expose
            var contactNumber: String? = null

            @SerializedName("telephone_number")
            @Expose
            var telephoneNumber: String? = null

            @SerializedName("category_id")
            @Expose
            var categoryId: Int? = null

            @SerializedName("status")
            @Expose
            var status: Int? = null

            @SerializedName("deleted_at")
            @Expose
            var deletedAt: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null

            @SerializedName("branch")
            @Expose
            var branch: Branch? = null

            @SerializedName("category")
            @Expose
            var category: Category? = null

            inner class Branch {
                @SerializedName("id")
                @Expose
                var id: Int? = null

                @SerializedName("user_id")
                @Expose
                var userId: Int? = null

                @SerializedName("state_id")
                @Expose
                var stateId: Int? = null

                @SerializedName("branch_name")
                @Expose
                var branchName: String? = null

                @SerializedName("contact_name")
                @Expose
                var contactName: String? = null

                @SerializedName("last_name")
                @Expose
                var lastName: String? = null

                @SerializedName("town")
                @Expose
                var town: String? = null

                @SerializedName("user_email_address")
                @Expose
                var userEmailAddress: String? = null

                @SerializedName("google_email")
                @Expose
                var googleEmail: String? = null

                @SerializedName("address")
                @Expose
                var address: String? = null

                @SerializedName("contact_number")
                @Expose
                var contactNumber: String? = null

                @SerializedName("teliphone_number")
                @Expose
                var teliphoneNumber: String? = null

                @SerializedName("status")
                @Expose
                var status: Int? = null

                @SerializedName("deleted_at")
                @Expose
                var deletedAt: String? = null

                @SerializedName("created_at")
                @Expose
                var createdAt: String? = null

                @SerializedName("updated_at")
                @Expose
                var updatedAt: String? = null

                @SerializedName("mon_from")
                @Expose
                var monFrom: String? = null

                @SerializedName("mon_to")
                @Expose
                var monTo: String? = null

                @SerializedName("tue_from")
                @Expose
                var tueFrom: String? = null

                @SerializedName("tue_to")
                @Expose
                var tueTo: String? = null

                @SerializedName("wed_from")
                @Expose
                var wedFrom: String? = null

                @SerializedName("wed_to")
                @Expose
                var wedTo: String? = null

                @SerializedName("thu_from")
                @Expose
                var thuFrom: String? = null

                @SerializedName("thu_to")
                @Expose
                var thuTo: String? = null

                @SerializedName("fri_from")
                @Expose
                var friFrom: String? = null

                @SerializedName("fri_to")
                @Expose
                var friTo: String? = null

                @SerializedName("sat_from")
                @Expose
                var satFrom: String? = null

                @SerializedName("sat_to")
                @Expose
                var satTo: String? = null

                @SerializedName("sun_from")
                @Expose
                var sunFrom: String? = null

                @SerializedName("sun_to")
                @Expose
                var sunTo: String? = null

                @SerializedName("public_mon_from")
                @Expose
                var publicMonFrom: String? = null

                @SerializedName("public_mon_to")
                @Expose
                var publicMonTo: String? = null

                @SerializedName("public_tue_from")
                @Expose
                var publicTueFrom: String? = null

                @SerializedName("public_tue_to")
                @Expose
                var publicTueTo: String? = null

                @SerializedName("public_wed_from")
                @Expose
                var publicWedFrom: String? = null

                @SerializedName("public_wed_to")
                @Expose
                var publicWedTo: String? = null

                @SerializedName("public_thu_from")
                @Expose
                var publicThuFrom: String? = null

                @SerializedName("public_thu_to")
                @Expose
                var publicThuTo: String? = null

                @SerializedName("public_fri_from")
                @Expose
                var publicFriFrom: String? = null

                @SerializedName("public_fri_to")
                @Expose
                var publicFriTo: String? = null

                @SerializedName("public_sat_from")
                @Expose
                var publicSatFrom: String? = null

                @SerializedName("public_sat_to")
                @Expose
                var publicSatTo: String? = null

                @SerializedName("public_sun_from")
                @Expose
                var publicSunFrom: String? = null

                @SerializedName("public_sun_to")
                @Expose
                var publicSunTo: String? = null
            }

            inner class Category {
                @SerializedName("id")
                @Expose
                var id: Int? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("is_active")
                @Expose
                var isActive: Int? = null

                @SerializedName("created_at")
                @Expose
                var createdAt: String? = null

                @SerializedName("updated_at")
                @Expose
                var updatedAt: String? = null

                @SerializedName("icon")
                @Expose
                var icon: String? = null
            }
        }
    }
}