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
    mavenLocal()
}

dependencies {
    implementation(gradleApi())

    implementation("net.lingala.zip4j:zip4j:2.11.6")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("commons-io:commons-io:2.21.0")
    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("org.apache.commons:commons-compress:1.28.0")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}