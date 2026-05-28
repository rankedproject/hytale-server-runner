package wtf.ranked.hytale.server.runner;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.plugins.ExtensionContainer;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.registrar.GradleServiceRegistrar;
import wtf.ranked.hytale.server.runner.registrar.task.GlobalTaskRegistrar;
import wtf.ranked.hytale.server.runner.registrar.task.StepTaskRegistrar;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.step.impl.DownloadModStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadServerAssetStep;
import wtf.ranked.hytale.server.runner.step.impl.LaunchServerStep;
import wtf.ranked.hytale.server.runner.step.impl.PrepareDownloaderStep;
import wtf.ranked.hytale.server.runner.task.global.LaunchServerTask;

/**
 * Main entry point for the Hytale Server Runner Gradle plugin.
 * <p>
 * This plugin initializes the project environment by configuring extensions,
 * registering required shared build services, and setting up the task hierarchy
 * needed to manage the Hytale server lifecycle.
 */
@NullMarked
public abstract class HytaleServerRunnerPlugin implements Plugin<Project> {

    public static final String GROUP = "hytaleRunner";
    public static final String GLOBAL_TASK_GROUP = "hytale runner";
    public static final String INTERNAL_TASK_GROUP = "hytale runner lifecycle";

    @Override
    public final void apply(final Project project) {
        final ExtensionContainer extensions = project.getExtensions();
        final ProjectLayout layout = project.getLayout();
        final HytalePluginExtension pluginExtension = extensions.create(GROUP, HytalePluginExtension.class, layout);

        serviceSetup(project);
        stepTaskSetup(project);
        globalTaskSetup(project, pluginExtension);
    }

    /**
     * Registers internal Gradle build services required to facilitate server operations.
     * <p>
     * These services, such as the {@link HttpResourceProvider}, are managed by the
     * {@link GradleServiceRegistrar} and made available for task execution.
     *
     * @param project the current project instance
     */
    private void serviceSetup(final Project project) {
        final GradleServiceRegistrar serviceRegistrar = new GradleServiceRegistrar(project);
        serviceRegistrar.register("httpResourceProvider", HttpResourceProvider.class);
    }

    /**
     * Registers the public-facing tasks that users interact with to manage the server.
     * <p>
     * This includes tasks like {@code launchServer}, which serves as the primary
     * entry point for running the server instance.
     *
     * @param project         the current project instance
     * @param pluginExtension the configuration extension providing project-specific settings
     */
    private void globalTaskSetup(final Project project, final HytalePluginExtension pluginExtension) {
        final GlobalTaskRegistrar taskRegistrar = new GlobalTaskRegistrar(project, pluginExtension);
        taskRegistrar.register("launchServer", LaunchServerTask.class);
    }

    /**
     * Configures the internal lifecycle tasks responsible for individual server setup steps.
     * <p>
     * Registers discrete units of work such as downloading assets, mod management,
     * and environment preparation using the {@link StepTaskRegistrar}.
     *
     * @param project the current project instance
     */
    private void stepTaskSetup(final Project project) {
        final StepTaskRegistrar taskRegistrar = new StepTaskRegistrar(project);
        taskRegistrar.register("downloadModStep", DownloadModStep.class);
        taskRegistrar.register("downloadServerAssetStep", DownloadServerAssetStep.class);
        taskRegistrar.register("launchServerStep", LaunchServerStep.class);
        taskRegistrar.register("prepareDownloaderStep", PrepareDownloaderStep.class);
    }
}