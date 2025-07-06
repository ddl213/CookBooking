import org.gradle.api.attributes.Attribute
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.cookbooking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cookbooking"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 注入模块名称
//        buildConfigField("String", "MODULE_NAME", "\"app\"")
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
    // 添加以下配置块解决变体选择问题
//    configurations.all {
//        resolutionStrategy {
//            eachDependency {
//                if (requested.group == "com.example" && requested.name == "lib_router") {
//                    useVersion("+")
//                    because("强制匹配 lib_router 的 debug 变体")
//                }
//            }
//        }
//    }

    // 解决资源冲突
//    packaging {
//        resources {
//            excludes += "META-INF/AL2.0"
//            excludes += "META-INF/LGPL2.1"
//            excludes += "META-INF/licenses/**"
//            pickFirsts += "META-INF/services/*"
//        }
//    }

}
//afterEvaluate {
//    configurations.ksp {
//        attributes {
//            val buildTypeAttr = Attribute.of("com.android.build.api.attributes.BuildTypeAttr", String::class.java)
//            attribute(buildTypeAttr, "debug")
//        }
//    }
//}
//kotlin {
//    sourceSets {
//        debug {
//            kotlin.srcDir("build/generated/ksp/debug/kotlin")
//        }
//        release {
//            kotlin.srcDir("build/generated/ksp/release/kotlin")
//        }
//    }
//}
dependencies {
    //导入模块
    implementation(project(":lib_common"))
    implementation(project(":lib_index"))
    // 项目路由模块
//    implementation(project(":lib_router"))
//
//    // KSP 处理器
//    ksp(project(":lib_router", "default"))

    // 主题与资源相关的依赖必须保留在 app 层
    implementation(libs.androidx.appcompat)


    // 测试依赖（可选）
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// KSP 配置
//ksp {
//    arg("router.target_package", "com.example.cookbooking.access")
//}