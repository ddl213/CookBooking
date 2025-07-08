plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.cook.booking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cook.booking"
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
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/debug/kotlin")
        }
    }

    // KSP 配置
    ksp {
        arg("MODULE_NAME", "app")
        arg("ROUTER_PACKAGE", "com.cook.booking.generated")
        arg("ROOT_DIR", rootDir.toPath().toString())
        arg("ksp.logging", "debug")
    }
}

dependencies {
    //导入模块
    implementation(project(":lib_common"))
    implementation(project(":lib_index"))

    // 项目路由模块
    implementation(project(":lib_router"))
    implementation(project(":lib_router_annotations"))
    // KSP 注解处理器的依赖
    ksp(project(":lib_router_compiler"))


    implementation(libs.androidx.appcompat)

}
