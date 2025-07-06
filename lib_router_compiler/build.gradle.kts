plugins {
    id("org.jetbrains.kotlin.jvm") // 纯 Kotlin JVM 库插件
    id("com.google.devtools.ksp") // KSP 插件
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":lib_router_annotations"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")
    implementation("com.squareup:kotlinpoet:1.16.0")
    implementation("com.squareup:kotlinpoet-ksp:1.16.0")
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib"))
}

ksp {
    arg("router.target_package", "app.access")
}