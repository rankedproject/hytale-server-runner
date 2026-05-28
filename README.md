<div align="center">

  <h1>Hytale Server Runner</h1>

  <p>
    A high-performance Gradle orchestration engine for Hytale server administrators and developers.
  </p>

<!-- Badges -->
<p>
  <a href="https://github.com/rankedproject/hytale-server-runner/issues">
    <img src="https://img.shields.io/github/issues/rankedproject/hytale-server-runner" alt="open issues" />
  </a>
  <a href="https://github.com/rankedproject/hytale-server-runner/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/rankedproject/hytale-server-runner.svg" alt="license" />
  </a>
</p>
</div>

---

# 📔 Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Installation](#installation)
- [Usage](#usage)
    - [Configuration](#configuration)
    - [Available Tasks](#available-tasks)
- [Hot Swapping](#hot-swapping)
- [Directory Structure](#directory-structure)

---

<a id="about-the-project"></a>
## About the Project

**Hytale Server Runner** is a specialized Gradle plugin that transforms your IDE into a comprehensive Hytale development environment. 
It automates the entire process of downloading server binaries and assets, allowing you to launch and debug your server instance 
directly from your IDE with a single click. The plugin handles all environment setup routine and provides a powerful Hot Swapping tool that applies code changes instantly without restarting the server, significantly accelerating your development iteration cycles.

<a id="features"></a>

### Features

* **Lifecycle Automation:** Automates fetching native assets, unpacking server files, and managing remote mods.
* **Module Integration:** Seamlessly link your internal project modules or external repositories to the server execution
  task.
* **Hot Swapping:** Runtime mod lifecycle reloads change code and see results instantly without restarting the server.
* **Flexible Sourcing:** Built-in support for MavenCentral, GitHub Releases, and direct URL mod sourcing.
* **Precision Control:** Fine-grained management of JVM arguments and networking.
* **Configuration Friendly:** Supports standard and flat directory structures via configuration without breaking your
  build logic.
* **Auto-Detection:** Automatic run configuration detection and environment setup.

<a id="getting-started"></a>

## Getting Started

<a id="installation"></a>

### Installation

Add the plugin to your `build.gradle.kts` (Kotlin) or `build.gradle` (Groovy):

```kotlin
plugins {
    id("wtf.ranked.hytale-server-runner") version "1.4.0"
}

```

## Usage

### Configuration

Configure your environment using the `hytaleServer` extension:

```kotlin
hytaleServer {
    mods {
        url("[https://example.com/mods/AwesomeMod.jar](https://example.com/mods/AwesomeMod.jar)", "AwesomeMod")
        github("owner", "repository", "tag", "assetName.jar")
    }

    serverAddress("127.0.0.1", 25565)
    serverOnlineMode(OnlineMode.OFFLINE)
    serverJar.set(file("bin/HytaleServer.jar"))
    assets.set(file("bin/Assets.jar"))

    jvmArgs.addAll("-Xmx4G", "-Xms2G", "-XX:+UseG1GC")

    runDirectory.set(layout.projectDirectory.dir("run"))
    serverDirectory.set(layout.projectDirectory.dir("server"))
    modDirectory.set(layout.projectDirectory.dir("mods"))
    serverJarMainClass.set("com.hypixel.hytale.Main")
    serverDownloadUri.set(URI.create("[https://downloader.hytale.com/hytale-downloader.zip](https://downloader.hytale.com/hytale-downloader.zip)"))
    downloadTimeout.set(Duration.ofSeconds(10))

    dependsOn("help", "testClasses")
    patchline.set(Patchline.PRE_RELEASE)

    environment("databaseName", "mongodb-project")
}

```

### Available Tasks

| Task           | Description                                                                                                        |
|----------------|--------------------------------------------------------------------------------------------------------------------|
| `launchServer` | The Daily Driver. Checks for updates, verifies mods, and launches the server. Supports `-Ddebug` for hot-swapping. |

Run via terminal:

```bash
./gradlew launchServer

```

## Hot Swapping

Simply click the **Debug icon (the "bug")** in your IDE to start the `launchServer` task. Once the server is running,
any time you change your code, trigger a build/compile in your IDE. The agent will detect the changes and reload your
plugin classes instantly.

## Directory Structure

* `/run` - Execution context, live server logs, and configs.
* `/run/server` - Core Hytale server binaries and libraries.
* `/run/mods` - Managed mods directory (local and remote jars).