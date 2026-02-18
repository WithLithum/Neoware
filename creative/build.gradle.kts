plugins {
    java
}

group = "x.withlithum.neoware"
version = "2026.1.0-alpha.1"

repositories {
    mavenCentral()
}

dependencies {
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}