plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.aplicacionmovil"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.aplicacionmovil"
        minSdk = 24
        targetSdk = 36
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // Iconos extendidos de Material (necesario para VisibilityOff)
    implementation("androidx.compose.material:material-icons-extended:1.7.5")

    // Retrofit para llamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Moshi para JSON
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

    // OkHttp logging (para ver requests/responses en Logcat)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.7")


    // Librería necesaria para usar Theme.Material3 en themes.xml
    implementation("com.google.android.material:material:1.12.0")

    // SplashScreen API
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    // Navegación
    implementation("androidx.navigation:navigation-compose:2.8.3")
    
    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.3.0")
    
    // Testing en Debug (para Previews)
    debugImplementation("androidx.navigation:navigation-testing:2.8.3")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
