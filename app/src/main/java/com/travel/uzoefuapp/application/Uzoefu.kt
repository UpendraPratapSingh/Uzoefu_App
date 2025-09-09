package com.travel.uzoefuapp.application

import android.app.Application
import android.content.res.Configuration
import com.travel.uzoefuapp.utils.PreferenceManager

class Uzoefu : Application() {
    companion object {
        lateinit var encryptedPrefs: PreferenceManager
        lateinit var instance: Uzoefu
    }

    override  fun onCreate() {
        super.onCreate()
        encryptedPrefs = PreferenceManager(applicationContext).getInstance(applicationContext)
        instance = this



        /* // ✅ Initialize Mastercard Gateway SDK
         CoroutineScope(Dispatchers.IO).launch {
             GatewaySDK.initialize(
                 this@Safer,
                 "TEST100532051",
                 "Safer",
                 "https://afs.gateway.mastercard.com/ma/",
                 GatewayRegion.MTF
             )
         }*/

        // FirebaseApp.initializeApp(this) // Uncomment if needed
    }

    fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}
