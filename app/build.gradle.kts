plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "com.rhys.main"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rhys.main"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.1"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    ksp {
        arg("MODULE_NAME", "main")
        arg("ROOT_DIR", rootDir.toPath().toString())
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }
}

dependencies {
    getRootDir().list()?.forEach {
        if ("subModules" == it) {
            File(getRootDir(), it).list()?.forEach { child ->
                if (child.startsWith("lib_") && !child.contains("compiler")) {
                    println("subModules : ${child}")
                    implementation(project(path = ":$child"))
                }
            }
        }
    }
    ksp(project(":lib_route_compiler"))

    implementation(libs.androidx.appcompat)





    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}