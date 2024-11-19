plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.gms.google.services)
}

android {
    namespace = "ucne.edu.proyectofinalaplicada2"
    compileSdk = 35


    defaultConfig {
        applicationId = "ucne.edu.proyectofinalaplicada2"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    //navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.firebase.common.ktx)
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    //optional
    implementation("androidx.room:room-ktx:2.6.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    ksp("com.google.dagger:hilt-android-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //Test
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation(libs.junit)
    testImplementation("com.google.truth:truth:1.4.2")
    testImplementation("app.cash.turbine:turbine:1.1.0")

    //Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.1")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.1")
    implementation("io.coil-kt:coil-compose:2.2.2")

    //Firebase
    implementation(libs.firebase.bom)
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)




        implementation(libs.androidx.material.icons.extended)

    //Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

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



}