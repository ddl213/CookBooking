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
    // 修正 KSP 生成文件路径配置
    sourceSets {
        getByName("main") {
            // 确保这里指向 KSP 生成的 .kt 或 .java 文件的父目录
            // 根据你的图片，可能是 'build/generated/ksp/debug/kotlin/kspDebugKotlin'
            java.srcDir("build/generated/ksp/debug/kotlin/kspDebugKotlin")
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

    // 显式添加 lib_router_annotations 依赖，确保 KSP 能够正确解析注解
    implementation(project(":lib_router_annotations"))

    // KSP 注解处理器的依赖
    ksp(project(":lib_router_compiler"))
    // 额外添加注解模块作为 KSP 的处理目标，确保 KSP 能直接看到注解定义
    ksp(project(":lib_router_annotations"))


    // 主题与资源相关的依赖必须保留在 app 层
    implementation(libs.androidx.appcompat)


}
// KSP 配置
ksp {
    arg("router.target_package", "app.access")
    // 添加调试日志
    arg("ksp.logging", "debug")
}
