package com.travel.uzoefuapp.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class PreferenceManager(context: Context) {
    private var mPrefs: PreferenceManager? = null

    /*  private val masterKeyAlias: String = MasterKey.DEFAULT_MASTER_KEY_ALIAS
      private val masterKey: MasterKey =
          MasterKey.Builder(context, masterKeyAlias).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
              .build()


      private val prefs = EncryptedSharedPreferences.create(
          context, "YourEncryptedPreferencesFileName",
          masterKey,
          EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
          EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
      )*/

    private var masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    /*    private var prefs = EncryptedSharedPreferences.create(
            context,
            "myprefrence",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )*/

    private val prefs = EncryptedSharedPreferences.create(
        context, "myprefrence",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val editor = prefs.edit()

    @OptIn(InternalCoroutinesApi::class)
    fun getInstance(context: Context): PreferenceManager {
        if (mPrefs == null) {
            synchronized(PreferenceManager::class.java) {
                if (mPrefs == null) mPrefs = PreferenceManager(context)
            }
        }
        return mPrefs!!
    }

    // region "Getters & Setters"
    var isFirstTime: Boolean
        get() = prefs.getBoolean(IS_FIRST_TIME, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME, isFirstTime)
            editor.apply()
        }

    var appLanguage: String
        get() = prefs.getString(USER_LANG, "") ?: ""
        set(appLanguage) {
            editor.putString(USER_LANG, appLanguage)
            editor.apply()
        }

    var token: String
        get() = prefs.getString(BEARER_TOKEN, "") ?: ""
        set(bearerToken) {
            editor.putString(BEARER_TOKEN, bearerToken)
            editor.apply()
        }

    var floossToken: String
        get() = prefs.getString(FLOOSS_TOKEN, "") ?: ""
        set(token) {
            editor.putString(FLOOSS_TOKEN, token)
            editor.apply()
        }
    var floossTokenExpiry: Long
        get() = prefs.getLong(FLOOSS_TOKEN_EXP, 0)
        set(token) {
            editor.putLong(FLOOSS_TOKEN_EXP, token)
            editor.apply()
        }
    var floossTokenPayment: String
        get() = prefs.getString(FLOOSS_TOKENS, "") ?: ""
        set(token) {
            editor.putString(FLOOSS_TOKENS, token)
            editor.apply()
        }
    var languageIds: String
        get() = prefs.getString(languagesId, "") ?: ""
        set(languageId) {
            editor.putString(languagesId, languageId)
            editor.apply()
        }
    var bearerToken: String
        get() = prefs.getString(BEARER_USER_TOKEN, "") ?: ""
        set(userToken) {
            editor.putString(BEARER_USER_TOKEN, userToken)
            editor.apply()
        }
    var userId: String
        get() = prefs.getString(USER_ID, "") ?: ""
        set(userId) {
            editor.putString(USER_ID, userId)
            editor.apply()
        }
    var visaType: String
        get() = prefs.getString(hostTypes, "") ?: ""
        set(hostType) {
            editor.putString(hostTypes, hostType)
            editor.apply()
        }
    var hostingId: String
        get() = prefs.getString(hostTypeIds, "") ?: ""
        set(hostingId) {
            editor.putString(hostTypeIds, hostingId)
            editor.apply()
        }
    var currency: String
        get() = prefs.getString(currencyIds, "") ?: ""
        set(currency) {
            editor.putString(currencyIds, currency)
            editor.apply()
        }
    var statusDone: String
        get() = prefs.getString(status, "") ?: ""
        set(statusDone) {
            editor.putString(status, statusDone)
            editor.apply()
        }
    var countryId: String
        get() = prefs.getString(country, "") ?: ""
        set(countryId) {
            editor.putString(country, countryId)
            editor.apply()
        }
    var passengerId: String
        get() = prefs.getString(passengerIds, "") ?: ""
        set(passengerId) {
            editor.putString(passengerIds, passengerId)
            editor.apply()
        }
    var visaId: String
        get() = prefs.getString(state, "") ?: ""
        set(cityId) {
            editor.putString(state, cityId)
            editor.apply()
        }
    var FCMToken: String
        get() = prefs.getString(FCM_TOKEN, "") ?: ""
        set(userToken) {
            editor.putString(FCM_TOKEN, userToken)
            editor.apply()
        }
    var isNotification: Boolean
        get() = prefs.getBoolean(IS_NOTIFICATION, true)
        set(isNotification) {
            editor.putBoolean(IS_NOTIFICATION, isNotification)
            editor.apply()
        }
    var availableDateId: String
        get() = prefs.getString(availableDatesId, "") ?: ""
        set(dateId) {
            editor.putString(availableDatesId, dateId)
            editor.apply()
        }
    var selectedPaymentId: String
        get() = prefs.getString(selectPaymentId, "") ?: ""
        set(paymentId) {
            editor.putString(selectPaymentId, paymentId)
            editor.apply()
        }
    var selectedActivityId: String
        get() = prefs.getString(activityId, "") ?: ""
        set(activityId) {
            editor.putString(activityId, activityId)
            editor.apply()
        }
    var availableSeat: Int
        get() = prefs.getInt(passengersId.toString(), 0) ?: 0
        set(passengerId) {
            editor.putInt(passengersId.toString(), passengerId)
            editor.apply()
        }
    var isVerified: String
        get() = prefs.getString(isVerifieds, "") ?: ""
        set(isVerified) {
            editor.putString(isVerifieds, isVerified)
            editor.apply()
        }
    var title: String
        get() = prefs.getString(officialDocs, "") ?: ""
        set(officialDoc) {
            editor.putString(officialDocs, officialDoc)
            editor.apply()
        }
    var installmentPaymentPrice: String
        get() = prefs.getString(paymenytPrice, "") ?: ""
        set(paymenytPrice) {
            editor.putString(paymenytPrice, paymenytPrice)
            editor.apply()
        }
    var clickValue: String
        get() = prefs.getString(ClickValue, "") ?: ""
        set(ClickValues) {
            editor.putString(ClickValue, ClickValues)
            editor.apply()
        }

    companion object {
        // region "Tags"
        private const val IS_FIRST_TIME = "isFirstTime"
        private const val languagesId = "languageId"
        private const val BEARER_TOKEN = "bearerToken"
        private const val BEARER_USER_TOKEN = "BEARER_USER_TOKEN"
        private const val USER_ID = "USER_ID"
        private const val hostTypes = "hostType"
        private const val hostTypeIds = "hostTypeIds"
        private const val currencyIds = "currencyIds"
        private const val FLOOSS_TOKEN = "floossToken"
        private const val FLOOSS_TOKENS = "floossTokens"
        private const val FLOOSS_TOKEN_EXP = "floossTokensExpiry"
        private const val passengerIds = "passengerId"
        private const val status = "status"
        private const val country = "country"
        private const val browse = "browse"
        private const val state = "state"
        private const val email = "email"
        private const val activityTitle = "activityTitle"
        private const val isVerifieds = "isVerifieds"
        private const val officialDocs = "officialDocs"
        private const val paymenytPrice = "paymenytPrice"
        private const val ClickValue = "clickValue"
        private const val USER_PREFS = "USER_PREFS"
        private const val USER_LANG = "USER_LANG"
        private const val DEVICE_ID = "DEVICE_ID"
        private const val FCM_TOKEN = "FCM_TOKEN"
        private const val IS_NOTIFICATION = "IS_NOTIFICATION"
        private const val availableDatesId = "availableDatesId"
        private const val selectPaymentId = "paymentId"
        private const val activityId = "activityId"
        private const val passengersId = 0
    }
}