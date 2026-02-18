plugins {
    java
    application
}

group = "x.withlithum.neoware"
version = "2026.1.0-alpha.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)

    // Minestom PvP
    implementation(libs.minestom.pvp)

    runtimeOnly(libs.bundles.log4j.runtime)

    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.junit.jupiter)
}

application {
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=ALL-UNNAMED",
        "-Dstdout.encoding=UTF-8",
        "-Dstderr.encoding=UTF-8"
    )
    mainClass = "x.withlithum.neoware.adventure.Program"
}

tasks.test {
    useJUnitPlatform()
}