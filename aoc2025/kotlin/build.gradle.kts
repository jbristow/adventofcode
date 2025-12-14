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
val joptVersion = "1.4.3"
val cplexVersion = "12.8"
dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")
    implementation("edu.harvard.eecs:jopt:$joptVersion")
    runtimeOnly(files("/Users/jbristow/Applications/CPLEX_Studio_Community2212/cplex/lib/cplex.jar"))
    testImplementation(kotlin("test"))
}

tasks.withType<JavaExec> {
    systemProperty("java.library.path", "/Users/jbristow/Applications/CPLEX_Studio_Community2212/cplex/bin/arm64_osx")
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
