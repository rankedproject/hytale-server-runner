plugins {
    java
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
    compileOnly(gradleApi())
    compileOnly(libs.lombok)

    implementation(libs.zip4j)
    implementation(libs.apache.commons.io)
    implementation(libs.apache.commons.compress)
    implementation(libs.guava)

    annotationProcessor(libs.lombok)
}