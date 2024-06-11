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
    implementation ("com.konghq:unirest-java:3.13.6")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation ("org.apache.commons:commons-text:1.10.0")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("gg.jte:jte:3.1.10")
    implementation("org.slf4j:slf4j-simple:2.0.9")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.7.2")

    implementation("io.javalin:javalin-bundle:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("io.javalin:javalin-testtools:6.1.3")

    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation ("org.assertj:assertj-core:3.24.2")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))

    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports { xml.required.set(true) }
}