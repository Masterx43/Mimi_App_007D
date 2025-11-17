plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

android {
    namespace = "com.example.uinavegacion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.uinavegacion"
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
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //librerias nuevas
    implementation("androidx.navigation:navigation-compose:2.8.0")         // Navegación Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4") // ViewModel para Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")   // Ciclo de vida Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")       // Extensiones de ciclo de vida
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1") // Corrutinas Android
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.compose.material3:material3:1.3.0")

    //Data Store Preferncias
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //cargar imagenes (para mostrarlas en la ui)
    implementation("io.coil-kt:coil-compose:2.7.0")

    //nuevas bibliotecas para la evaluacion 3

    // ==== AGREGADOS PARA REST ====
    // Retrofit base
    implementation("com.squareup.retrofit2:retrofit:2.11.0") // <-- NUEVO
    // Convertidor JSON con Gson
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // <-- NUEVO
    // OkHttp y logging interceptor
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // <-- NUEVO
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // <-- NUEVO

    //librerias de Test Locales
    testImplementation(libs.junit) //libreria junit
    testImplementation("io.mockk:mockk:1.13.12") //Mock para kotlin
    testImplementation("org.robolectric:robolectric:4.13") //simular pruebas en Android test locales
    //test implementacion UI
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    //librerias para el manejo de reglas de test
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("androidx.test:rules:1.5.0")






}