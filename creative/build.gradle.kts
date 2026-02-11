plugins {
    kotlin("jvm")
}

group = "x.withlithum.neoware"
version = "2026.1.0-alpha.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}