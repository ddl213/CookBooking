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

    // 如果您的注解使用 Kotlinx Serialization 或其他需要库支持的特性，在这里添加
    // 例如：
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.x.x")
}
