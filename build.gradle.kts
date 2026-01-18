import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    alias(libs.plugins.kotlin.gradlePluginJvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleVersionsPlugin)
    alias(libs.plugins.mavenPublish)
}

group = "nl.wykorijnsburger.kminrandom"
version = "2.0.0"

kotlin {
    explicitApi()
}

repositories {
    mavenCentral()
}

dependencies {
    detektPlugins(libs.detekt.ktlint)

    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(libs.assertk)
}

// Ensure "org.gradle.jvm.version" is set to "17" in Gradle metadata.
tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

mavenPublishing {
    publishToMavenCentral()

    // Only sign when credentials are available (CI/release builds)
    if (project.hasProperty("signing.keyId") || System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey") != null) {
        signAllPublications()
    }

    coordinates("nl.wykorijnsburger.kminrandom", "kminrandom", version.toString())

    pom {
        name.set("KMinRandom")
        description.set("A library for generating minimal random instances of Kotlin data classes")
        url.set("https://github.com/utwyko/KMinRandom")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("wyko")
                name.set("Wyko Rijnsburger")
            }
        }
        scm {
            url.set("https://github.com/utwyko/KMinRandom")
            connection.set("scm:git:git://github.com/utwyko/KMinRandom.git")
            developerConnection.set("scm:git:ssh://github.com/utwyko/KMinRandom.git")
        }
    }
}

detekt {
    buildUponDefaultConfig = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
}
