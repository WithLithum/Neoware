plugins {
    kotlin("jvm")
}

group = "x.withlithum.neoware"
version = "2026.1.0-alpha.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation(libs.kotlinlogging)

    // Minestom PvP
    implementation(libs.minestom.pvp)

    compileOnly(libs.bundles.log4j.runtime)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}