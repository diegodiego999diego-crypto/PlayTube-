plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.github.play.playtube"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.play.playtube"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Deja solo arm64 (los teléfonos modernos) para no triplicar peso
        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // UI Jetpack Compose & Material 3 Expressive
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.1")

    // Miniaturas
    implementation("io.coil-kt:coil-compose:2.6.0")

    // NewPipe Extractor (Feed, datos, streaming)
    implementation("com.github.TeamNewPipe:NewPipeExtractor:v0.24.6")

    // --- LA LIBRERÍA EXACTA QUE USA SEAL (SIN EL BINARIO FFmpeg) ---
    implementation("io.github.junkfood02.youtubedl-android:library:0.17.2")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}