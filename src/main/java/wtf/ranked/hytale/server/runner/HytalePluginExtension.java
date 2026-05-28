package wtf.ranked.hytale.server.runner;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.extension.OnlineMode;
import wtf.ranked.hytale.server.runner.extension.Patchline;
import wtf.ranked.hytale.server.runner.mod.ModExtension;

import javax.inject.Inject;
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
@NullMarked
public abstract class HytalePluginExtension implements Serializable {

    /**
     * Configures the server mods via an action.
     *
     * @param action configuration block for mods
     */
    public void mods(final Action<? super ModExtension> action) {
        action.execute(getModExtension());
    }

    /**
     * Adds an environment variable to the server process.
     *
     * @param identifier variable name
     * @param value      variable value
     */
    public void environment(final String identifier, final Object value) {
        getServerEnvironment().put(identifier, value);
    }

    /**
     * Adds the name of the prerequisite build task using a string identifier.
     *
     * @param taskName the name of the task to depend on (e.g., "assemble")
     */
    public void dependsOn(final String... taskName) {
        getDependsOn().addAll(taskName);
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
    public void serverAddress(final String host, final int port) {
        getServerAddress().set(new InetSocketAddress(host, port));
    }

    /**
     * Accesses the nested configuration for server mods.
     *
     * @return the nested mod configuration extension
     */
    @Nested
    public abstract ModExtension getModExtension();

    /**
     * Map of environment variables passed to the server process.
     *
     * @return the property containing environment variables
     */
    public abstract MapProperty<String, Object> getServerEnvironment();

    /**
     * Directory where the Hytale server files are located.
     *
     * @return the property containing the server directory
     */
    public abstract DirectoryProperty getServerDirectory();

    /**
     * Root directory for server execution.
     *
     * @return the property containing the run directory
     */
    public abstract DirectoryProperty getRunDirectory();

    /**
     * Directory where server mods will be installed.
     *
     * @return the property containing the mods directory
     */
    public abstract DirectoryProperty getModDirectory();

    /**
     * File property for the server assets archive.
     *
     * @return the property containing the assets file
     */
    public abstract RegularFileProperty getAssets();

    /**
     * URI used to download the server software.
     *
     * @return the property containing the server download URI
     */
    public abstract Property<URI> getServerDownloadUri();

    /**
     * The primary executable JAR file for the server.
     *
     * @return the property containing the server JAR file
     */
    public abstract RegularFileProperty getServerJar();

    /**
     * The entry point class name for the Hytale server.
     *
     * @return the property containing the server main class name
     */
    public abstract Property<String> getServerJarMainClass();

    /**
     * List of JVM arguments to pass to the server process.
     *
     * @return the property containing the list of JVM arguments
     */
    public abstract ListProperty<String> getServerJvmArgs();

    /**
     * Property which allows to change the server online mode.
     *
     * @return the property containing the server online mode
     */
    public abstract Property<OnlineMode> getServerOnlineMode();

    /**
     * The InetSocketAddress to which the server will be bound.
     *
     * @return the property containing the server bind address
     */
    public abstract Property<InetSocketAddress> getServerAddress();

    /**
     * The maximum amount of time allowed for download operations.
     * <p>
     * This timeout is applied when downloading server software or assets to prevent
     * the build process from hanging indefinitely due to network issues.
     *
     * @return the property containing the download timeout duration
     */
    public abstract Property<Duration> getDownloadTimeout();

    /**
     * The names of the tasks that must be completed before the server runs.
     * <p>
     * These properties define dependencies for the server execution pipeline.
     * Usually, this includes tasks like {@code jar} or {@code shadowJar} to
     * ensure that the latest version of the project is compiled and packaged
     * before the server starts.
     *
     * @return the property containing the list of prerequisite build tasks
     */
    public abstract ListProperty<String> getDependsOn();

    /**
     * Returns the targeted patchline (e.g., RELEASE, SNAPSHOT) for the server.
     *
     * @return a property containing the {@link Patchline}
     */
    public abstract Property<Patchline> getPatchline();

    /**
     * Constructs a new HytalePluginExtension and sets default conventions.
     *
     * @param layout the Gradle project layout used to resolve default paths
     */
    @Inject
    public HytalePluginExtension(final Project project, final ProjectLayout layout) {
        getPatchline().convention(Patchline.RELEASE);
        getRunDirectory().convention(getPatchline().map(line -> layout.getProjectDirectory().dir("run/" + line.getIdentifier())));
        getServerDirectory().convention(getRunDirectory().dir("Server"));
        getModDirectory().convention(getServerDirectory().dir("mods"));
        getServerJar().convention(getServerDirectory().file("HytaleServer.jar"));
        getAssets().convention(getRunDirectory().file("Assets.zip"));
        getServerDownloadUri().convention(URI.create("https://downloader.hytale.com/hytale-downloader.zip"));
        getServerJarMainClass().convention("com.hypixel.hytale.Main");
        getServerOnlineMode().convention(OnlineMode.AUTHENTICATED);
        getServerAddress().convention(new InetSocketAddress("0.0.0.0", 5520));
        getServerJvmArgs().convention(new ArrayList<>());
        getServerEnvironment().convention(new HashMap<>());
        getDownloadTimeout().set(Duration.ofSeconds(20));
        getDependsOn().convention(new ArrayList<>()).add("jar");
    }
}
