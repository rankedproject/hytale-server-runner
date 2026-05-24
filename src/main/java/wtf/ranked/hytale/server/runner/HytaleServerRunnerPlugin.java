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
 * Gradle plugin for booting a Hytale server.
 * <p>
 * Registers the necessary services and tasks to manage the server
 * lifecycle, primarily through the {@code launchServer} task.
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
        project.afterEvaluate(_ -> {
            stepTaskSetup(project);
            globalTaskSetup(project, pluginExtension);
        });
    }

    /**
     * Registers internal build services required for the server environment.
     *
     * @param project current project instance
     */
    private void serviceSetup(final Project project) {
        final GradleServiceRegistrar serviceRegistrar = new GradleServiceRegistrar(project);
        serviceRegistrar.register("httpResourceProvider", HttpResourceProvider.class);
    }

    /**
     * Sets up the global tasks for server interaction.
     * <p>
     * Registers {@code launchServer} to start the server instance.
     *
     * @param project current project instance
     * @param pluginExtension the extension used to configure the runner
     */
    private void globalTaskSetup(final Project project, final HytalePluginExtension pluginExtension) {
        final GlobalTaskRegistrar taskRegistrar = new GlobalTaskRegistrar(project, pluginExtension);
        taskRegistrar.register("launchServer", LaunchServerTask.class);
    }

    private void stepTaskSetup(final Project project) {
        final StepTaskRegistrar taskRegistrar = new StepTaskRegistrar(project);
        taskRegistrar.register("downloadModStep", DownloadModStep.class);
        taskRegistrar.register("downloadServerAssetStep", DownloadServerAssetStep.class);
        taskRegistrar.register("launchServerStep", LaunchServerStep.class);
        taskRegistrar.register("prepareDownloaderStep", PrepareDownloaderStep.class);
    }
}
