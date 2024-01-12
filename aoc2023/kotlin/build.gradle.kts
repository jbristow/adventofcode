plugins {
    kotlin("jvm") version "1.9.21"
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val arrowVersion = "1.2.1"
val jupiterVersion = "5.10.1"
val jacksonVersion = "2.13.4"
dependencies {

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:$arrowVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("org.assertj:assertj-core:3.24.2")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation(files("/Users/jbristow/git/z3/build/com.microsoft.z3.jar"))
    implementation(files("/Users/jbristow/git/z3/build/libz3.dylib"))
    implementation(files("/Users/jbristow/git/z3/build/libz3java.dylib"))
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(20)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperties =
        mapOf(
            "junit.jupiter.extensions.autodetection.enabled" to true,
            "junit.jupiter.testinstance.lifecycle.default" to "per_class",
            "junit.jupiter.execution.parallel.enabled" to true,
            "junit.jupiter.execution.parallel.mode.default" to "concurrent",
            "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent",
        )
    testLogging {
        events("passed", "skipped", "failed")
    }
}
