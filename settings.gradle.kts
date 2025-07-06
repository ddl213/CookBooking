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

}


rootProject.name = "CookBooking"
include(":app")
include(":lib_common")
include(":lib_router")
include(":lib_index")
include(":lib_router_compiler")
include(":lib_router_annotations")
