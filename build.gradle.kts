import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.gradleVersionsPlugin)
    alias(libs.plugins.gradleKtLintPlugin)
}

group = "nl.wykorijnsburger.kminrandom"
version = "1.0.4"

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        browser()
        nodejs()
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

repositories {
    mavenCentral()
}

// Ensure "org.gradle.jvm.version" is set to "8" in Gradle metadata.
tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

task<Jar>("javadocJar") {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "kminrandom"
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                name.set("KMinRandom")
                description.set("A library for generating minimal random instances of Kotlin data classes")
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
                url.set("https://github.com/utwyko/KMinRandom")
                scm {
                    url.set("https://github.com/utwyko/KMinRandom")
                    connection.set("scm:git:git://github.com/utwyko/KMinRandom.git")
                    developerConnection.set("scm:git:ssh://example.com/my-library.git")
                }
            }
        }
        repositories {
            maven {
                name = "sonatype"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = findProperty("NEXUS_USERNAME") as String?
                    password = findProperty("NEXUS_PASSWORD") as String?
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
