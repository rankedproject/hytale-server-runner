package net.rankedproject.hytale.boot;

import net.rankedproject.hytale.boot.mod.registry.ModDownloaderStrategyRegistry;
import net.rankedproject.hytale.boot.mod.strategy.UrlDownloaderStrategy;
import net.rankedproject.hytale.boot.resource.HttpResourceProvider;
import net.rankedproject.hytale.boot.task.global.LaunchServerTask;
import net.rankedproject.hytale.boot.task.global.UpdateServerTask;
import net.rankedproject.hytale.boot.task.registrar.GlobalTaskRegistrar;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.services.BuildServiceRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HytaleBootPlugin implements Plugin<Project> {

    public static final Logger LOGGER = LoggerFactory.getLogger(HytaleBootPlugin.class);
    public static final String PLUGIN_GROUP = "hytale";

    @Override
    public void apply(final @NotNull Project project) {
        final ExtensionContainer extensions = project.getExtensions();
        final HytaleBootExtension hytaleBootExtension = extensions.create(PLUGIN_GROUP, HytaleBootExtension.class);

        serviceSetup(project, hytaleBootExtension);
        gradleTaskSetup(project);
    }

    // TODO: refactor it. Make service setuper I think?
    private void serviceSetup(final @NotNull Project project, final @NotNull HytaleBootExtension bootExtension) {
        final BuildServiceRegistry serviceRegistry = project.getGradle().getSharedServices();
        serviceRegistry.registerIfAbsent("httpResourceProvider", HttpResourceProvider.class);
        serviceRegistry.registerIfAbsent("urlDownloaderStrategy", UrlDownloaderStrategy.class, spec -> {
            spec.getParameters().getModDirectory().set(bootExtension.getModDirectory());
        });
        serviceRegistry.registerIfAbsent("modDownloaderStrategyRegistry", ModDownloaderStrategyRegistry.class);
    }

    private void gradleTaskSetup(final @NotNull Project project) {
        final GlobalTaskRegistrar taskRegistrar = new GlobalTaskRegistrar(project);
        taskRegistrar.register("launchServer", LaunchServerTask.class);
        taskRegistrar.register("updateServer", UpdateServerTask.class);
    }
}