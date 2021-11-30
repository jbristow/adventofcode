import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val arrowVersion = "0.11.0"
val jupiterVersion = "5.7.0"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    // Use the Kotlin test library
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    // Use the Kotlin JUnit integration
    testImplementation(kotlin("test-junit"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("org.assertj:assertj-core:3.18.1")

    implementation("com.github.almasb:grammy:0.0.2")
    implementation("org.antlr:antlr4:4.9")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperties = mapOf(
        "junit.jupiter.extensions.autodetection.enabled" to true,
        "junit.jupiter.testinstance.lifecycle.default" to "per_class",
        "junit.jupiter.execution.parallel.enabled" to true,
        "junit.jupiter.execution.parallel.mode.default" to "concurrent",
        "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent"
    )
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
