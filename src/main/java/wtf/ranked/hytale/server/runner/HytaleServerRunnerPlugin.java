package wtf.ranked.hytale.server.runner;

import wtf.ranked.hytale.server.runner.registrar.GlobalTaskRegistrar;
import wtf.ranked.hytale.server.runner.registrar.GradleServiceRegistrar;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.task.global.LaunchServerTask;
import wtf.ranked.hytale.server.runner.task.global.UpdateServerTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jspecify.annotations.NonNull;

/**
 * Gradle plugin for booting a Hytale server.
 * <p>
 * Registers the necessary services and tasks to manage the server
 * lifecycle, primarily through the {@code launchServer} task.
 */
public abstract class HytaleServerRunnerPlugin implements Plugin<Project> {

    public static final String PLUGIN_GROUP = "hytaleServerRunner";

    @Override
    public final void apply(final @NonNull Project project) {
        project.getExtensions().create(PLUGIN_GROUP, HytalePluginExtension.class, project.getLayout());
        serviceSetup(project);
        taskSetup(project);
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
    private void taskSetup(final @NonNull Project project) {
        final GlobalTaskRegistrar taskRegistrar = new GlobalTaskRegistrar(project);
        taskRegistrar.register("launchServer", LaunchServerTask.class);
        taskRegistrar.register("updateServer", UpdateServerTask.class);
    }
}
