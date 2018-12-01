plugins {
    `java-library`
    kotlin("jvm") version Versions.kotlin
    id("com.github.ben-manes.versions") version Versions.benManesVersions
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(Libs.junitJupiterApi)
    testRuntimeOnly(Libs.junitJupiterEngine)
    testImplementation(Libs.assertJCore)
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

repositories {
    mavenCentral()
}
