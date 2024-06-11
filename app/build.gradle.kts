plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("jacoco")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.4"
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
    implementation("io.javalin:javalin-bundle:5.6.3")
    implementation("io.javalin:javalin-rendering:5.6.3")

    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("com.h2database:h2:2.2.220")
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("gg.jte:jte:3.1.9")
    implementation("commons-validator:commons-validator:1.8.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports { xml.required.set(true) }
}