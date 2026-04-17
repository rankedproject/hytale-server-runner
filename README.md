# Hytale Boot Gradle Plugin

[](https://plugins.gradle.org/)

**Hytale Boot** is a high-performance orchestration engine for Hytale server administrators and developers. It automates the tedious lifecycle of a server—from fetching native assets and managing remote mods to fine-tuning JVM execution.

-----

## Features

  * **Automated Provisioning**: Downloads and unpacks Hytale server files and assets automatically.
  * **Mod Management**: Seamlessly pull mods from direct URLs or GitHub releases.
  * **Environment Control**: Fine-grained control over JVM arguments, server networking, and directory structures.
  * **Task-Based Workflow**: Simplified Gradle tasks to launch or fresh-reinstall your server environment.

-----

## Installation

Add the plugin to your `build.gradle.kts` (Kotlin) or `build.gradle` (Groovy) file:

```kotlin
plugins {
    id("rankedproject.hytale.boot") version "1.0"
}
```

-----

## 🛠️ Configuration

Configure your environment using the `hytaleBoot` extension. This block defines how your server behaves and where it sources its data.

```kotlin
hytaleBoot {
    // --- Mod Management ---
    mods {
        // Fetch from a direct link
        url("https://example.com/mods/AwesomeMod.jar", "AwesomeMod")
        
        // Fetch directly from GitHub Releases
        github("owner", "repository", "tag", "assetName.jar")
        github("rankedproject", "hytale-boot", "1.0", "hytale-boot-plugin.jar")
    }

    // --- Server Settings ---
    serverAddress("127.0.0.1", 25565)
    serverOnlineMode(OnlineMode.OFFLINE) // Or OnlineMode.AUTHENTICATED
    serverJar.set(file("bin/HytaleServer.jar"))
    assets.set(file("bin/Assets.jar"))
    
    // --- JVM Optimization ---
    jvmArgs.addAll("-Xmx2G", "-Xms1G", "-XX:+UseG1GC")

    // --- Advanced Paths & Sources ---
    serverDownloadUri.set(uri("https://downloader.hytale.com/hytale-downloader.zip"))
    serverJarMainClass.set("com.hypixel.hytale.Main")
    
    runDirectory.set(layout.projectDirectory.dir("run"))
    serverDirectory.set(layout.projectDirectory.dir("server"))
    modDirectory.set(layout.projectDirectory.dir("mods"))
    downloadTimeout.set(Duration.ofSeconds(50))

    // Custom environment variables
    environment("databaseName", "mongodb-project")
}
```

-----

## Available Tasks

The plugin registers two primary tasks under the `hytale` group:

| Task | Description |
| :--- | :--- |
| `launchServer` | **The Daily Driver.** Checks for existing files. If missing, it downloads assets, unpacks the server, and fetches mods before launching. If files exist, it starts the server immediately. |
| `updateServer` | **The Fresh Start.** Wipes all existing server files and performs a clean installation of the server, assets, and modifications. |

### How to run:

```bash
./gradlew launchServer
```

-----

## 📂 Directory Structure

By default, the plugin organizes your workspace as follows:

  * `/run`: Execution context and live server logs.
  * `/run/Server`: Core Hytale server binaries and libraries.
  * `/run/Server/mods`: Downloaded `.jar` files managed by the `mods` block.