package wtf.ranked.hytale.server.runner.registrar.task;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.registrar.Registrar;
import wtf.ranked.hytale.server.runner.step.TaskStepLoader;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;

/**
 * Handles the registration and lifecycle setup of Global tasks.
 * <p>
 * Implements {@link Registrar} to provide a standardized way of
 * adding orchestrator tasks to the project. It automatically
 * configures the task's execution queue by resolving its required steps.
 */
@NullMarked
public final class GlobalTaskRegistrar implements Registrar<GlobalRunningTask> {

    private final Project project;
    private final TaskStepLoader taskStepLoader;

    public GlobalTaskRegistrar(final Project project, final HytalePluginExtension pluginExtension) {
        this.project = project;
        this.taskStepLoader = new TaskStepLoader(project, pluginExtension);
    }

    @Override
    public void register(final String name, final Class<? extends GlobalRunningTask> value) {
        final TaskProvider<? extends GlobalRunningTask> taskProvider = project.getTasks().register(name, value);
        taskStepLoader.setup(taskProvider.get());
    }
}
