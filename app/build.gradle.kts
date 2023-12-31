import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
    id("kotlin-parcelize")
}

val apiKeyPropertiesFile = rootProject.file("apikey.properties")
val apiKeyProperties = Properties().apply {
    load(FileInputStream(apiKeyPropertiesFile))
}

android {
    namespace = "com.fkuper.metronome"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fkuper.metronome"
        minSdk = 24
        targetSdk = 33
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

        defaultConfig {
            buildConfigField("String", "SPOTIFY_WEB_API_SECRET", apiKeyProperties.getProperty("SPOTIFY_WEB_API_SECRET"))
            buildConfigField("String", "SPOTIFY_WEB_API_CLIENT_ID", apiKeyProperties.getProperty("SPOTIFY_WEB_API_CLIENT_ID"))
            buildConfigField("String", "SPOTIFY_WEB_API_URL", apiKeyProperties.getProperty("SPOTIFY_WEB_API_URL"))
            buildConfigField("String", "SPOTIFY_WEB_API_VERSION", apiKeyProperties.getProperty("SPOTIFY_WEB_API_VERSION"))
            buildConfigField("String", "SPOTIFY_WEB_API_AUTH_URL", apiKeyProperties.getProperty("SPOTIFY_WEB_API_AUTH_URL"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
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
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.chargemap.compose:numberpicker:1.0.3")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    implementation("com.google.guava:guava:31.1-android")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("androidx.lifecycle:lifecycle-service:2.6.2")

    // Room
    val roomVersion = "2.6.0"
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}