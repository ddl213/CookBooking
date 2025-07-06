plugins {
    id("org.jetbrains.kotlin.jvm") // 纯 Kotlin JVM 库插件
    id("com.google.devtools.ksp") // KSP 插件
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")
//    implementation(project(":lib_router")) // 用于访问注解定义
    implementation("com.squareup:kotlinpoet:1.16.0")
    implementation("com.squareup:kotlinpoet-ksp:1.16.0")
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib"))
    implementation(project(":lib_router_annotations"))
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17" // 使用一个被支持的 JVM 版本
}
ksp {
    arg("router.target_package", "app.access")
}