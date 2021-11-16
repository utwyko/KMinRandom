rootProject.name = "kminrandom"

// Enable Gradle's version catalog support
// https://docs.gradle.org/current/userguide/platforms.html
enableFeaturePreview("VERSION_CATALOGS")

// Plugin versions cannot be specified in the versions catalog yet, see https://melix.github.io/blog/2021/03/version-catalogs-faq.html
pluginManagement {
    plugins {
        kotlin("jvm") version "1.6.0"
        id("com.github.ben-manes.versions") version "0.39.0"
        id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    }
}
