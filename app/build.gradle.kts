plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt") // Ensure kapt plugin is included
    id("dagger.hilt.android.plugin") // Hilt plugin applied
    id ("kotlin-parcelize")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.majorproject.roomify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.majorproject.roomify"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true  // Enable code shrinking in release build
            isShrinkResources = true  // Enable resource shrinking
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17" // Update from "1.8" to "17"
    }

    buildFeatures {
        dataBinding =true
    }


    // Enable vector drawables
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    // Updated libraries to the latest stable versions for better optimization and security
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.google.android.material:material:1.12.0")  // Updated Material library
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation ("com.google.dagger:hilt-android:2.49")
    implementation("androidx.paging:paging-common-android:3.3.6")
    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.1")
    kapt       ("com.google.dagger:hilt-android-compiler:2.49"   ) // Test and UI testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("com.airbnb.android:lottie:5.0.3")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1") // LiveData (optional, if you need LiveData)

    // Lifecycle runtime (for viewModels() and other lifecycle features)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Dagger Hilt for Dependency Injection (for ViewModel injection)
    // Dependency injection
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")

    implementation( "androidx.paging:paging-runtime:3.2.1")
    implementation ("androidx.paging:paging-compose:3.2.1")   // optional, for Compose


    // Navigation and ImageView libraries
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")  // Updated Kotlin version

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.2")
    implementation ("com.github.Foysalofficial:NafisBottomNav:5.0")
    implementation("io.github.sceneview:arsceneview:2.3.0")

    implementation("com.google.ar:core:1.45.0")
    implementation ("com.jraska:falcon:2.2.0")
//firebase
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation("androidx.activity:activity-ktx:1.7.0") // Make sure this is included for `viewModels()`
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

}

