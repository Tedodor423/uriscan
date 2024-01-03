plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    //id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

android {
    namespace = "com.nimrichtr.uriscan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nimrichtr.uriscan"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")

    // Camera
    implementation("androidx.camera:camera-camera2:1.3.0-rc01")
    implementation("androidx.camera:camera-lifecycle:1.3.0-rc01")
    implementation("androidx.camera:camera-view:1.3.0-rc01")
    implementation("androidx.camera:camera-extensions:1.3.0-rc01")

    // Accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    /*// DEPENDENCY INJECTION
    implementation("io.insert-koin:koin-core:3.4.2")
    implementation("io.insert-koin:koin-android:3.4.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.6")
    // KSP
    implementation("io.insert-koin:koin-annotations:1.2.2")
    ksp("io.insert-koin:koin-ksp-compiler:1.2.2")*/

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //implementation("com.google.firebase:firebase-analytics")

    // Authentication
    implementation("com.google.firebase:firebase-auth")
    //implementation("com.google.android.gms:play-services-auth:20.7.0")

}