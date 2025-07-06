pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/releases") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://www.jitpack.io") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.namespace == "com.google.devtools.ksp") {
//                useModule("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${requested.version}")
//            }
//        }
//    }
}

//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        maven { url = uri("https://maven.aliyun.com/repository/public") }
//        maven { url = uri("https://maven.aliyun.com/repository/releases") }
//        maven { url = uri("https://maven.aliyun.com/repository/google") }
//        maven { url = uri("https://www.jitpack.io") }
//        google()
//        mavenCentral()
//    }
//}
rootProject.name = "CookBooking"
include(":app")
include(":lib_common")
include(":lib_router")
include(":lib_index")
include(":lib_router_compiler")
include(":lib_router_annotations")
