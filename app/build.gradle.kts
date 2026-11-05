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

        // Obligatorio para yt-dlp y ffmpeg (compila binarios para arquitectura ARM)
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        // Obligatorio para NewPipe Extractor en minSdk < 33
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
            pickFirsts += "lib/**/libc++_shared.so"
        }
    }
}

dependencies {
    // Permite características modernas de Java que requiere NewPipe
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // UI Jetpack Compose & Material 3
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.1")

    // Cargar miniaturas de videos
    implementation("io.coil-kt:coil-compose:2.6.0")

    // NewPipe Extractor (Streaming, feed, metadatos)
    implementation("com.github.TeamNewPipe:NewPipeExtractor:v0.24.6")

    // Librería yt-dlp (Misma que usa SEAL para descargas)
    implementation("com.github.yausername.youtubedl-android:library:0.15.0")
    implementation("com.github.yausername.youtubedl-android:ffmpeg:0.15.0")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}