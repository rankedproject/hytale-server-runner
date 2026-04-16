package net.rankedproject.hytale.boot;

import net.rankedproject.hytale.boot.mod.ModExtension;
import org.gradle.api.Action;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.net.URI;

/**
 * Main configuration extension for the Hytale Boot plugin.
 * <p>
 * Defines paths for the server environment, download locations,
 * and JVM arguments required to boot the server.
 */
@SuppressWarnings("unused")
public abstract class HytaleBootExtension implements Serializable {

    /**
     * Configures the server mods via an action.
     * * @param action configuration block for mods
     */
    public void mods(final @NotNull Action<? super ModExtension> action) {
        action.execute(getModExtension());
    }

    /**
     * Adds an environment variable to the server process.
     *
     * @param identifier variable name
     * @param value variable value
     */
    public void environment(final @NotNull String identifier, final @NotNull Object value) {
        getEnvironment().put(identifier, value);
    }

    /**
     * Accesses the nested configuration for server mods.
     * * @return mod configuration extension
     */
    @Nested
    public abstract @NotNull ModExtension getModExtension();

    /**
     * Map of environment variables passed to the server process.
     */
    public abstract @NotNull MapProperty<String, Object> getEnvironment();

    /**
     * Directory where the Hytale server files are located.
     */
    public abstract @NotNull DirectoryProperty getServerDirectory();

    /**
     * Root directory for server execution.
     */
    public abstract @NotNull DirectoryProperty getRunDirectory();

    /**
     * Directory where server mods will be installed.
     */
    public abstract @NotNull DirectoryProperty getModDirectory();

    /**
     * File property for the server assets archive.
     */
    public abstract @NotNull Property<File> getAssets();

    /**
     * URI used to download the server software.
     */
    public abstract @NotNull Property<URI> getServerDownloadUri();

    /**
     * The primary executable JAR file for the server.
     */
    public abstract @NotNull Property<File> getServerJar();

    /**
     * The entry point class name for the Hytale server.
     */
    public abstract @NotNull Property<String> getServerJarMainClass();

    /**
     * List of JVM arguments to pass to the server process.
     */
    public abstract @NotNull ListProperty<String> getJvmArgs();

    @Inject
    public HytaleBootExtension(final @NotNull ProjectLayout layout) {
        getRunDirectory().convention(layout.getProjectDirectory().dir("run"));
        getServerDirectory().convention(getRunDirectory().dir("Server"));
        getModDirectory().convention(getServerDirectory().dir("mods"));
        getServerJar().convention(getServerDirectory().file("HytaleServer.jar").map(RegularFile::getAsFile));
        getAssets().convention(getRunDirectory().file("Assets.zip").map(RegularFile::getAsFile));
        getServerDownloadUri().convention(URI.create("https://downloader.hytale.com/hytale-downloader.zip"));
        getServerJarMainClass().convention("com.hypixel.hytale.Main");
    }
}
