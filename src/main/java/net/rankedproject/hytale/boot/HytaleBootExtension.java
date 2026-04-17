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
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

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
     *
     * @param action configuration block for mods
     */
    public void mods(final @NonNull Action<? super ModExtension> action) {
        action.execute(this.getModExtension());
    }

    /**
     * Adds an environment variable to the server process.
     *
     * @param identifier variable name
     * @param value      variable value
     */
    public void environment(final @NonNull String identifier, final @NonNull Object value) {
        this.getEnvironment().put(identifier, value);
    }

    /**
     * Sets the network address and port for the Hytale server.
     * <p>
     * This is a convenience method that creates an {@link InetSocketAddress}
     * and updates the {@link #getServerAddress()} property.
     *
     * @param host the hostname or IP address to bind to (e.g., "0.0.0.0")
     * @param port the port number (e.g., 5520)
     */
    public void serverAddress(final @NonNull String host, final int port) {
        this.getServerAddress().set(new InetSocketAddress(host, port));
    }

    /**
     * Accesses the nested configuration for server mods.
     *
     * @return the nested mod configuration extension
     */
    @Nested
    public abstract @NonNull ModExtension getModExtension();

    /**
     * Map of environment variables passed to the server process.
     *
     * @return the property containing environment variables
     */
    public abstract @NonNull MapProperty<String, Object> getEnvironment();

    /**
     * Directory where the Hytale server files are located.
     *
     * @return the property containing the server directory
     */
    public abstract @NonNull DirectoryProperty getServerDirectory();

    /**
     * Root directory for server execution.
     *
     * @return the property containing the run directory
     */
    public abstract @NonNull DirectoryProperty getRunDirectory();

    /**
     * Directory where server mods will be installed.
     *
     * @return the property containing the mods directory
     */
    public abstract @NonNull DirectoryProperty getModDirectory();

    /**
     * File property for the server assets archive.
     *
     * @return the property containing the assets file
     */
    public abstract @NonNull Property<File> getAssets();

    /**
     * URI used to download the server software.
     *
     * @return the property containing the server download URI
     */
    public abstract @NonNull Property<URI> getServerDownloadUri();

    /**
     * The primary executable JAR file for the server.
     *
     * @return the property containing the server JAR file
     */
    public abstract @NonNull Property<File> getServerJar();

    /**
     * The entry point class name for the Hytale server.
     *
     * @return the property containing the server main class name
     */
    public abstract @NonNull Property<String> getServerJarMainClass();

    /**
     * List of JVM arguments to pass to the server process.
     *
     * @return the property containing the list of JVM arguments
     */
    public abstract @NonNull ListProperty<String> getJvmArgs();

    /**
     * Property which allows to change the server online mode.
     *
     * @return the property containing the server online mode
     */
    public abstract @NonNull Property<OnlineMode> getServerOnlineMode();

    /**
     * The InetSocketAddress to which the server will be bound.
     *
     * @return the property containing the server bind address
     */
    public abstract @NonNull Property<InetSocketAddress> getServerAddress();

    /**
     * The maximum amount of time allowed for download operations.
     * <p>
     * This timeout is applied when downloading server software or assets to prevent
     * the build process from hanging indefinitely due to network issues.
     *
     * @return the property containing the download timeout duration
     */
    public abstract @NonNull Property<Duration> getDownloadTimeout();

    /**
     * Constructs a new HytaleBootExtension and sets default conventions.
     *
     * @param layout the Gradle project layout used to resolve default paths
     */
    @Inject
    public HytaleBootExtension(final @NonNull ProjectLayout layout) {
        getRunDirectory().convention(layout.getProjectDirectory().dir("run"));
        getServerDirectory().convention(getRunDirectory().dir("Server"));
        getModDirectory().convention(getServerDirectory().dir("mods"));
        getServerJar().convention(getServerDirectory().file("HytaleServer.jar").map(RegularFile::getAsFile));
        getAssets().convention(getRunDirectory().file("Assets.zip").map(RegularFile::getAsFile));
        getServerDownloadUri().convention(URI.create("https://downloader.hytale.com/hytale-downloader.zip"));
        getServerJarMainClass().convention("com.hypixel.hytale.Main");
        getServerOnlineMode().convention(OnlineMode.AUTHENTICATED);
        getServerAddress().convention(new InetSocketAddress("0.0.0.0", 5520));
        getJvmArgs().convention(new ArrayList<>());
        getEnvironment().convention(new HashMap<>());
        getDownloadTimeout().set(Duration.ofSeconds(20));
    }
}
