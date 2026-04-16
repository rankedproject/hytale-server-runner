package net.rankedproject.hytale.boot;

import net.rankedproject.hytale.boot.mod.registry.ModDownloaderStrategyRegistry;
import net.rankedproject.hytale.boot.mod.strategy.UrlDownloaderStrategy;
import net.rankedproject.hytale.boot.registrar.GlobalTaskRegistrar;
import net.rankedproject.hytale.boot.registrar.GradleServiceRegistrar;
import net.rankedproject.hytale.boot.resource.HttpResourceProvider;
import net.rankedproject.hytale.boot.task.global.LaunchServerTask;
import net.rankedproject.hytale.boot.task.global.UpdateServerTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gradle plugin for booting a Hytale server.
 * <p>
 * Registers the necessary services and tasks to manage the server
 * lifecycle, primarily through the {@code launchServer} task.
 */
public abstract class HytaleBootPlugin implements Plugin<Project> {

    public static final Logger LOGGER = LoggerFactory.getLogger(HytaleBootPlugin.class);
    public static final String PLUGIN_GROUP = "hytale";

    @Override
    public void apply(final @NotNull Project project) {
        project.getExtensions().create("hytaleBoot", HytaleBootExtension.class, project.getLayout());
        serviceSetup(project);
        gradleTaskSetup(project);
    }

    /**
     * Registers internal build services required for the server environment.
     *
     * @param project current project instance
     */
    private void serviceSetup(final @NotNull Project project) {
        final GradleServiceRegistrar serviceRegistrar = new GradleServiceRegistrar(project);
        serviceRegistrar.register("httpResourceProvider", HttpResourceProvider.class);
        serviceRegistrar.register("modDownloaderStrategyRegistry", ModDownloaderStrategyRegistry.class);
        serviceRegistrar.register("urlDownloaderStrategy", UrlDownloaderStrategy.class);
    }

    /**
     * Sets up the global tasks for server interaction.
     * <p>
     * Registers {@code launchServer} to start the instance and
     * {@code updateServer} to prepare assets and the server environment.
     *
     * @param project current project instance
     */
    private void gradleTaskSetup(final @NotNull Project project) {
        final GlobalTaskRegistrar taskRegistrar = new GlobalTaskRegistrar(project);
        taskRegistrar.register("launchServer", LaunchServerTask.class);
        taskRegistrar.register("updateServer", UpdateServerTask.class);
    }
}