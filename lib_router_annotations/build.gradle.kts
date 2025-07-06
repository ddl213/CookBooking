plugins {
    id("org.jetbrains.kotlin.jvm") // 纯 Kotlin JVM 库
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    // 只需要 Kotlin 标准库
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib"))
}
