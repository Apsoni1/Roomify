plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.majorproject.roomify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.majorproject.roomify"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    // Test and UI testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Dependency injection
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")


    // Navigation and ImageView libraries
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")  // Updated Kotlin version

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")

}

