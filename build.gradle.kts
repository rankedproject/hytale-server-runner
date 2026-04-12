plugins {
    id("java")
    `kotlin-dsl`
    `maven-publish`
}

group = "net.cachewrapper"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        register("hytale-runner-plugin") {
            id = "net.cachewrapper.hytale.runner"
            implementationClass = "net.cachewrapper.hytale.runner.HytaleRunnerPlugin"
        }
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(gradleApi())
    implementation("net.lingala.zip4j:zip4j:2.11.6")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}