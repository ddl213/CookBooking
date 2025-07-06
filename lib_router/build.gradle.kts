plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lib_router"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = false // 如果不需要 BuildConfig 可关闭
    }

}

dependencies {
    api(project(":lib_router_annotations"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.junit.ktx)

    // KSP 依赖
//    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
//
//
//    // KotlinPoet 用于代码生成
//    implementation("com.squareup:kotlinpoet:1.16.0")
//    implementation("com.squareup:kotlinpoet-ksp:1.16.0")
}

