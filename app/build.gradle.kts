plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.asavault.asavaultsdkcontainer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.asavault.asavaultsdkcontainer"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //   Asa Vault sdk Library
    implementation("com.asavault:nativeLibrary:0.0.201-local")
    // Add Lottie dependency for react-native-lottie support
    implementation("com.airbnb.android:lottie:6.3.0")
    // Add biometric dependency for react-native-lottie support
    implementation("androidx.biometric:biometric:1.1.0")

    // Add Coil dependency for image loading (required by AsaVault SDK)
    implementation("io.coil-kt.coil3:coil:3.0.4")
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    // Add Firebase dependencies for react-native-firebase support
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation ("com.google.firebase:firebase-dynamic-links")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-config-ktx")

    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.media3:media3-common:1.4.1")
    implementation("androidx.media3:media3-datasource-okhttp:1.4.1")
    implementation("androidx.media3:media3-session:1.4.1")
    implementation("androidx.media3:media3-datasource:1.4.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // CRITICAL: Add this to fix the NoSuchMethodError
    implementation 'androidx.activity:activity-ktx:1.9.2'


}