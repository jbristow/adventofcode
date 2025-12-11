plugins {
    kotlin("jvm") version "2.2.21"
    id("dev.detekt") version "2.0.0-alpha.1"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(24)
}

val arrowVersion = "2.2.0"
val jupiterVersion = "6.0.1"
val assertjVersion = "3.27.6"
val coroutinesVersion = "1.10.2"
dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")

    testImplementation(kotlin("test"))
}
