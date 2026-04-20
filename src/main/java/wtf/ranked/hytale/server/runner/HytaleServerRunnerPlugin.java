package wtf.ranked.hytale.server.runner;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.plugins.ExtensionContainer;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.registrar.GlobalTaskRegistrar;
import wtf.ranked.hytale.server.runner.registrar.GradleServiceRegistrar;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.task.global.LaunchServerTask;
import wtf.ranked.hytale.server.runner.task.global.UpdateServerTask;

/**
 * Gradle plugin for booting a Hytale server.
 * <p>
 * Registers the necessary services and tasks to manage the server
 * lifecycle, primarily through the {@code launchServer} task.
 */
public abstract class HytaleServerRunnerPlugin implements Plugin<Project> {

    public static final String GROUP = "hytaleServer";

    @Override
    public final void apply(final @NonNull Project project) {
        final ExtensionContainer extensions = project.getExtensions();
        final ProjectLayout layout = project.getLayout();

        final HytalePluginExtension pluginExtension = extensions.create(GROUP, HytalePluginExtension.class, layout);
        serviceSetup(project);
        taskSetup(project, pluginExtension);
    }

    /**
     * Registers internal build services required for the server environment.
     *
     * @param project current project instance
     */
    private void serviceSetup(final @NonNull Project project) {
        final GradleServiceRegistrar serviceRegistrar = new GradleServiceRegistrar(project);
        serviceRegistrar.register("httpResourceProvider", HttpResourceProvider.class);
    }

    /**
     * Sets up the global tasks for server interaction.
     * <p>
     * Registers {@code launchServer} to start the instance and
     * {@code updateServer} to prepare assets and the server environment.
     *
     * @param project current project instance
     */
    private void taskSetup(final @NonNull Project project, final @NonNull HytalePluginExtension pluginExtension) {
        final GlobalTaskRegistrar taskRegistrar = new GlobalTaskRegistrar(project, pluginExtension);
        taskRegistrar.register("launchServer", LaunchServerTask.class);
        taskRegistrar.register("updateServer", UpdateServerTask.class);
    }
}
