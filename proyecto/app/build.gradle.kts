import org.gradle.api.JavaVersion

plugins {
    alias(libs.plugins.android.application) // Plugin para aplicaciones Android
    alias(libs.plugins.kotlin.android) // Soporte para Kotlin en Android
    alias(libs.plugins.kotlin.compose) // Habilita Jetpack Compose

    alias(libs.plugins.google.gms.plugin) // Plugin para servicios de Google (Firebase)
}

android {
    namespace = "com.example.vidasalud" // Identificador del paquete base
    compileSdk = 34 // Versión del SDK con la que compila la app

    defaultConfig {
        applicationId = "com.example.vidasalud" // ID único de la app
        minSdk = 24 // Mínima versión de Android soportada
        targetSdk = 34 // Versión objetivo de Android
        versionCode = 1 // Código interno de versión
        versionName = "1.0" // Nombre visible de versión

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Runner para tests
        vectorDrawables {
            useSupportLibrary = true // Soporte para vectores en versiones antiguas
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // No comprime/optimiza en release (ProGuard)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro" // Archivo personalizado ProGuard
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Compatibilidad Java 8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true // Habilita APIs modernas en SDKs bajos
    }

    kotlinOptions {
        jvmTarget = "1.8" // Target de Kotlin para JVM
    }

    buildFeatures {
        compose = true // Activa Jetpack Compose
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Evita conflictos de licencias
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Extensiones KTX
    implementation(libs.google.material) // Material Design
    implementation(libs.androidx.activity.compose) // Actividades con Compose
    implementation(platform(libs.androidx.compose.bom)) // BOM de Compose
    implementation(libs.androidx.ui) // UI Compose
    implementation(libs.androidx.ui.graphics) // Gráficos Compose
    implementation(libs.androidx.ui.tooling.preview) // Previsualización Compose
    implementation(libs.androidx.material3) // Material 3 Compose
    implementation(libs.androidx.material.icons.extended) // Iconos extendidos
    implementation(libs.androidx.navigation.compose) // Navegación con Compose
    implementation(libs.androidx.lifecycle.runtime.ktx) // Ciclo de vida Kotlin
    implementation(libs.androidx.lifecycle.viewmodel.compose) // ViewModel con Compose

    implementation(platform(libs.firebase.bom)) // Manejo de versiones Firebase
    implementation(libs.firebase.auth.ktx) // Firebase Auth
    implementation(libs.firebase.firestore.ktx) // Firestore

    implementation("io.coil-kt:coil-compose:2.6.0") // Carga de imágenes con Coil

    testImplementation(libs.junit) // Dependencias para tests unitarios
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling) // Herramientas de debug para Compose
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring(libs.desugar.jdk.libs) // Desugaring de librerías Java
}
