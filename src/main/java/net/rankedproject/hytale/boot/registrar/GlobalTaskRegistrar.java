package net.rankedproject.hytale.boot.registrar;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.StepContext;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
            final @NotNull String name,
            final @NotNull Class<? extends GlobalRunningTask> runningTask
    ) {
        final TaskProvider<? extends GlobalRunningTask> taskProvider = project.getTasks().register(name, runningTask);
        configureTask(taskProvider);
    }

    private void configureTask(final @NotNull TaskProvider<? extends GlobalRunningTask> provider) {
        final GlobalRunningTask task = provider.get();
        final List<Class<? extends Step>> steps = task.steps();

        final StepContext stepContext = new StepContext(steps, this.project);
        stepContext.setup(task);
    }
}