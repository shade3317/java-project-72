plugins {
    java
    application
    checkstyle
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"


application {
    mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.22.0")

    implementation("io.javalin:javalin:5.6.3")
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports { xml.required.set(true) }
}