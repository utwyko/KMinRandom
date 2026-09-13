import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    alias(libs.plugins.kotlin.gradlePluginJvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleVersionsPlugin)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlinx.benchmark)
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

// Benchmarks live in their own source set so they never end up in the published jar.
sourceSets {
    create("benchmark")
}

kotlin {
    target.compilations.getByName("benchmark")
        .associateWith(target.compilations.getByName("main"))
}

dependencies {
    "benchmarkImplementation"(libs.kotlinx.benchmark.runtime)
}

// JMH requires @State classes to be open.
allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("benchmark")
    }
    configurations {
        // Steady-state cost per call. For profilers (-prof gc, -prof async), run the JMH jar directly (see README).
        named("main") {
            exclude("ColdStartBenchmark")
            warmups = 5
            iterations = 5
            iterationTime = 1
            iterationTimeUnit = "s"
            mode = "avgt"
            outputTimeUnit = "ns"
            reportFormat = "json"
            advanced("jvmForks", 2)
        }
        // Quick check that every benchmark runs; numbers are not reliable.
        register("smoke") {
            exclude("ColdStartBenchmark")
            warmups = 1
            iterations = 1
            iterationTime = 200
            iterationTimeUnit = "ms"
            mode = "avgt"
            outputTimeUnit = "ns"
            advanced("jvmForks", 1)
        }
        // First call in a fresh JVM: one invocation per fork. The single-shot mode is set with an
        // annotation on ColdStartBenchmark, because this DSL only accepts thrpt and avgt.
        register("coldStart") {
            include("ColdStartBenchmark")
            warmups = 0
            iterations = 1
            outputTimeUnit = "ms"
            reportFormat = "json"
            advanced("jvmForks", 20)
        }
    }
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
