plugins {
    id ("java-library")
    id ("org.jetbrains.kotlin.jvm")
    id ("kotlin")
    id("com.google.devtools.ksp")
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":lib_router_annotations"))

    compileOnly("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")
    compileOnly("com.squareup:kotlinpoet:1.16.0")
    compileOnly("com.squareup:kotlinpoet-ksp:1.16.0")
    compileOnly("com.squareup:kotlinpoet-metadata:1.16.0")


}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}