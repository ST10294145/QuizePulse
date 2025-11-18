plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.saihilg.quizepulse"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.saihilg.quizepulse"
        minSdk = 29
        targetSdk = 36
        versionCode = 1  // increase this before submitting
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // APK
    signingConfigs {
        create("release") {
            storeFile = file("/Users/saihilgurupersad/Desktop/keystores/my-release-key.jks")
            storePassword = "Spirtwalker61"
            keyAlias = "key0"
            keyPassword = "Spirtwalker61"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Make sure release is NOT debuggable
            isDebuggable = false
        }

        debug {
            // keep debug normal
            isMinifyEnabled = false
            isDebuggable = true
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
    implementation("com.google.android.material:material:1.12.0")

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))

    // Firebase
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("androidx.biometric:biometric:1.2.0-alpha04")
    implementation(libs.androidx.work.runtime.ktx)

    // Unit testing
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:5.13.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:5.2.1")

    // Android instrumented (UI) testing
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // AndroidX + Jetpack
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
