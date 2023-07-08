import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.kotlin.gradlePluginJvm)
    alias(libs.plugins.gradleVersionsPlugin)
    alias(libs.plugins.gradleKtLintPlugin)
}

group = "nl.wykorijnsburger.kminrandom"
version = "1.0.4"

kotlin {
    explicitApi()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(libs.junitJupiter.api)
    testRuntimeOnly(libs.junitJupiter.engine)
    testImplementation(libs.assertjCore)
}

// Ensure "org.gradle.jvm.version" is set to "8" in Gradle metadata.
tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<Wrapper> {
    distributionType = Wrapper.DistributionType.ALL
}

task<Jar>("sourcesJar") {
    from(sourceSets.main.get().allJava)
    archiveClassifier.set("sources")
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
