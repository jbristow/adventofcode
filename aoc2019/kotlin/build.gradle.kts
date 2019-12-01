import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val arrowVersion = "0.10.3"
val jupiterVersion = "5.5.2"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    // Use the Kotlin test library
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1")

    // Use the Kotlin JUnit integration
    testImplementation(kotlin("test-junit"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testCompile("org.assertj:assertj-core:3.14.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

