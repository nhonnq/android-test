plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
}

android {
    namespace = "dev.nhonnq.test"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    namespace = "dev.nhonnq.test"
}

dependencies {
    // UnitTest
    api(libs.junit.ktx)
    api(libs.core.testing)
    api(libs.junit.junit)
    api(libs.kotlinx.coroutines.test)

    // Mockito
    api(libs.mockito.kotlin)
    api(libs.mockito.inline)
    api(libs.mockito.android)

    //
    api(libs.turbine)
    api(libs.truth)
}