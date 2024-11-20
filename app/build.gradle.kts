plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.dagger.hilt)
    kotlin("plugin.serialization") version "2.0.21"

//    alias(libs.plugins.google.firebase.crashlytics)
//    alias(libs.plugins.google.firebase.perf)
//    alias(libs.plugins.google.gms.services)
}

android {
    namespace = "ptit.vietpq.fitnessapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ptit.vietpq.fitnessapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.preferences.ktx)
    implementation(libs.google.material)

    // Navigation
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.compose)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.livedata.core)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.google.dagger.hilt.android)
    ksp(libs.google.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coroutine
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.test)
    implementation(libs.coroutines.play.services)

    // Image loading
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.ui.util)

    // firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.perf)

    // Network
    implementation(libs.bundles.retrofit.okhttp3)
    implementation(libs.bundles.moshi)

    // Utils
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)

    // CameraX
    implementation(libs.bundles.androidx.camera)

    // PermissionsState
    implementation(libs.accompanistPermissions)

    // lottie
    implementation(libs.lottie)

    implementation(libs.numberpicker)

    implementation(libs.androidx.multidex)
    implementation(libs.coil.compose)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
//  debugImplementation(libs.leakcanary)

    lintChecks(libs.slack.compose.lint)

    //ml kit common
    implementation(libs.pose.detection)
    implementation(libs.pose.detection.accurate)
    implementation(libs.azure.ai.inference)

    implementation(libs.flow.bus)
    implementation(libs.zoomable)
    implementation(libs.work.manager.runtime)
    implementation(libs.work.manager.ktx)

    implementation(libs.kotlinx.serialization.json)
    implementation (libs.exoplayer.core)
    implementation (libs.exoplayer.ui)
    implementation(libs.gson)
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.0")

    implementation ("com.github.jeziellago:compose-markdown:0.5.4")

}