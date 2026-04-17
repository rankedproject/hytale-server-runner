package net.rankedproject.hytale.boot.registrar;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.step.TaskStepLoader;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.jspecify.annotations.NonNull;

/**
 * Handles the registration and lifecycle setup of Global tasks.
 * <p>
 * Implements {@link Registrar} to provide a standardized way of
 * adding orchestrator tasks to the project. It automatically
 * configures the task's execution queue by resolving its required steps.
 */
@RequiredArgsConstructor
public final class GlobalTaskRegistrar implements Registrar<GlobalRunningTask> {

    private final Project project;

    @Override
    public void register(
            final @NonNull String name,
            final @NonNull Class<? extends GlobalRunningTask> runningTask
    ) {
        final TaskProvider<? extends GlobalRunningTask> taskProvider = project.getTasks().register(name, runningTask);
        configureTask(taskProvider);
    }

    private void configureTask(final @NonNull TaskProvider<? extends GlobalRunningTask> taskProvider) {
        final TaskStepLoader stepLoader = new TaskStepLoader(taskProvider.get(), this.project);
        stepLoader.setup();
    }
}
