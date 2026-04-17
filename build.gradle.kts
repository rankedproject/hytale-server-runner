plugins {
    java
    checkstyle
    `kotlin-dsl`
    `maven-publish`
}

group = "net.rankedproject.hytale"
version = "1.0"

gradlePlugin {
    plugins {
        register("hytale-boot-plugin") {
            id = "rankedproject.hytale.boot"
            implementationClass = "net.rankedproject.hytale.boot.HytaleBootPlugin"
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