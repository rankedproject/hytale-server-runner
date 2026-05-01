# Hytale Server Runner Gradle Plugin

**Hytale Server Runner** is a high-performance orchestration engine designed for Hytale server administrators and developers who demand speed, automation, and precision. It eliminates the tedious boilerplate of server management, allowing you to focus on growth and system architecture.

---

## Key Features

* Automates the entire lifecycle: fetching native assets, unpacking server files, and managing remote mods.
* Seamlessly link your internal project modules or external repositories to the server execution task.
* Runtime mod lifecycle reloads. Change your code and see results instantly without restarting the server.
* Built-in support for MavenCentral, GitHub Releases, and direct URL mod sourcing.
* Fine-grained control over JVM arguments, networking.
* Support for standard and flat directory structures via configuration without breaking your build logic.
* Automatic run configuration detection and environment setup.

---

## Installation

Add the plugin to your `build.gradle.kts` (Kotlin) or `build.gradle` (Groovy):

```kotlin
plugins {
    id("wtf.ranked.hytale-server-runner") version "1.1.0"
}
```

---

## Configuration

Configure your environment using the `hytaleServer` extension. This block is your command center for server behavior and data sourcing.

```kotlin
hytaleServer {
    // --- Mod Management ---
    mods {
        url("https://example.com/mods/AwesomeMod.jar", "AwesomeMod")
        github("owner", "repository", "tag", "assetName.jar")
    }

    // --- Server Settings ---
    serverAddress("127.0.0.1", 25565)
    serverOnlineMode(OnlineMode.OFFLINE) 
    serverJar.set(file("bin/HytaleServer.jar"))
    assets.set(file("bin/Assets.jar"))
    
    // --- JVM and Performance ---
    jvmArgs.addAll("-Xmx4G", "-Xms2G", "-XX:+UseG1GC")
    enableHotSwap = true // Requires JBR/DCEVM

    // --- Advanced Workspace Setup ---
    runDirectory.set(layout.projectDirectory.dir("run"))
    serverDirectory.set(layout.projectDirectory.dir("server"))
    modDirectory.set(layout.projectDirectory.dir("mods"))
    
    // Linking multi-module builds
    dependsOnBuildTask(TaskName.SHADOW_JAR) 
    
    environment("databaseName", "mongodb-project")
}
```

---

## Available Tasks

The plugin registers the following tasks under the `hytaleServer` group:

| Task | Description |
| :--- | :--- |
| `launchServer` | **The Daily Driver.** Checks for updates, verifies mods, and launches the server. Supports -Ddebug for hot-swapping. |
| `updateServer` | **The Fresh Start.** Wipes all existing server files and performs a clean installation. |

### How to run:
```bash
./gradlew launchServer
```

---

## Hot Swapping

Simply click the **Debug icon (the "bug")** in your IDE to start the `launchServer` task. Once the server is running, any time you change your code, just trigger a build/compile in your IDE. The agent will detect the changes and reload your plugin classes instantly. No manual reloads, no wasted time.

---

## Directory Structure

By default, the plugin organizes your workspace as follows:

* `/run` - Execution context, live server logs, and configs.
* `/run/server` - Core Hytale server binaries and libraries.
* `/run/mods` - Managed mods directory (local and remote jars).
