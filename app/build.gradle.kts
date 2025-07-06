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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    // ✅ 添加这个配置确保KSP生成文件被包含
    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/debug/ksp")
        }
    }

    // 解决资源冲突
    packaging {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
            excludes += "META-INF/licenses/**"
            pickFirsts += "META-INF/services/*"
        }
    }

}

dependencies {
    //导入模块
    implementation(project(":lib_common"))
    implementation(project(":lib_index"))
    // 项目路由模块
    // 路由运行时库的依赖
    implementation(project(":lib_router"))
//
    // KSP 注解处理器的依赖
    ksp(project(":lib_router_compiler"))


    // 主题与资源相关的依赖必须保留在 app 层
    implementation(libs.androidx.appcompat)


}
// KSP 配置
ksp {
    arg("router.target_package", "app.access")
    // 添加调试日志
    arg("ksp.logging", "debug")
}