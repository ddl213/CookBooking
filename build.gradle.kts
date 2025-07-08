// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven { url = uri("https://www.jitpack.io") }
        mavenCentral()
        maven { url = uri("'https://repo1.maven.org/maven2/") }
        google()
        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
    dependencies {

        val kotlinVersion = "1.9.20"
        val navVersion = "2.7.7"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath ("com.android.tools.build:gradle:7.3.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath ("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.20-1.0.14")
    }
}
allprojects {
    configurations.all {
        resolutionStrategy {
            force ("androidx.core:core-ktx:1.10.1")
        }
    }
    repositories {
        maven { url = uri("https://www.jitpack.io") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/releases") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://artifact.bytedance.com/repository/Volcengine/") }
        google()
        mavenCentral()
    }

}
