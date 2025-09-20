plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.travel.uzoefuapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.travel.uzoefuapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_DEBUG", "true")
            buildConfigField("String", "API_KEY", "\"https://mobappssolutions.in/uzoefu/api/\"")
            buildConfigField("String", "IMAGE_KEY", "\"https://mobappssolutions.in/uzoefu/api/\"")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.activity:activity:1.10.1")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.10.0")

    implementation("com.tbuonomo:dotsindicator:4.3")
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation("me.relex:circleindicator:2.1.6")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.material:material:1.1.0-alpha09")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.gcacace:signature-pad:1.3.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    /* Alert dependency*/
    implementation("io.github.tutorialsandroid:kalertdialog:20.3.6")
    implementation("com.github.TutorialsAndroid:progressx:6.0.19")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    // spinner
    implementation("com.github.skydoves:powerspinner:1.2.4")

    implementation("io.github.chaosleung:pinview:1.4.4")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")

    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.4")

    //Rx
    implementation("io.reactivex.rxjava2:rxjava:2.2.10")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-compiler:2.46")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")

    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    // use for zoom in and zoom out of the image
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    implementation("androidx.security:security-crypto:1.1.0-alpha04")

}