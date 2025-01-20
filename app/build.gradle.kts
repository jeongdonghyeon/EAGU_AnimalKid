import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.20"
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val dotenvFile = file(".env")
        val dotenvProps = Properties()
        if (dotenvFile.exists()) {
            dotenvFile.inputStream().use {
                dotenvProps.load(it)
            }
        }
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${dotenvProps["GOOGLE_CLIENT_ID"]}\"")
        buildConfigField("String", "SMTP_HOST", "\"${dotenvProps["SMTP_HOST"]}\"")
        buildConfigField("String", "SMTP_PORT", "\"${dotenvProps["SMTP_PORT"]}\"")
        buildConfigField("String", "FROM_EMAIL", "\"${dotenvProps["FROM_EMAIL"]}\"")
        buildConfigField("String", "PASSWORD", "\"${dotenvProps["PASSWORD"]}\"")
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
        viewBinding = true
        buildConfig = true
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation("androidx.activity:activity:1.7.2")
    implementation("androidx.transition:transition:1.4.1")

    implementation(libs.androidx.navigation.fragment.ktx.v253)
    implementation(libs.androidx.navigation.ui.ktx.v253)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Dagger
    ksp(libs.dagger.compiler)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v261)
    implementation(libs.androidx.lifecycle.livedata.ktx.v261)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.squareup.converter.gson)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // BCrypt
    implementation(libs.jbcrypt)

    // Credentials API
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

    // Java Mail
    implementation(libs.mail.android.mail)
    implementation(libs.android.activation)

    // Google / Firebase
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials.v130)
    implementation(libs.androidx.appcompat)

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)

    // Others
    implementation("com.prolificinteractive:material-calendarview:1.4.3")
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")
}
