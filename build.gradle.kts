plugins {
    java
    checkstyle
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.1.1"
}

group = "wtf.ranked.hytale.server.runner"
version = "1.0.0"

gradlePlugin {
    website = "https://github.com/rankedproject/hytale-server-runner"
    vcsUrl = "https://github.com/rankedproject/hytale-server-runner.git"

    plugins {
        register("hytale-server-runner") {
            displayName = "Hytale Server Runner Plugin"
            id = "wtf.ranked.hytale-server-runner"
            implementationClass = "wtf.ranked.hytale.server.runner.HytaleServerRunnerPlugin"
            description = "High-performance Gradle tool for Hytale mod bootstrapping."
            tags = listOf("hytale", "launcher", "runner", "mod-development", "automation", "imperial")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    "checkstyle"(libs.stylecheck)
    compileOnly(gradleApi())
    compileOnly(libs.lombok)

    implementation(libs.vavr)
    implementation(libs.jspecify)
    implementation(libs.zip4j)
    implementation(libs.apache.commons.io)
    implementation(libs.apache.commons.compress)
    implementation(libs.guava)

    annotationProcessor(libs.lombok)
}

extensions.configure<CheckstyleExtension> {
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")

    isIgnoreFailures = true
    isShowViolations = true
    toolVersion = libs.versions.checkstyle.get()
}

tasks.withType<Checkstyle> {
    configDirectory.set(rootProject.file("config/checkstyle"))

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    source = fileTree("src") {
        include("**/*.java")
        exclude("**/generated/**")
    }
}