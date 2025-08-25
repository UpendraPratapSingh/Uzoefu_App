plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.activity:activity:1.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.google.android.material:material:1.10.0")

    implementation ("com.tbuonomo:dotsindicator:4.3")

    implementation ("androidx.media3:media3-exoplayer:1.3.1")

    // UI for PlayerView
    implementation ("androidx.media3:media3-ui:1.3.1")

    implementation ("me.relex:circleindicator:2.1.6")

    implementation ("com.google.android.material:material:1.12.0")

    implementation ("com.google.android.material:material:1.10.0")

    //view pager
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    //circle image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.google.android.material:material:1.1.0-alpha09")

    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

}