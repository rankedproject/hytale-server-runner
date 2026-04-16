package net.rankedproject.hytale.boot.task.registrar;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.StepContext;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public final class GlobalTaskRegistrar {

    private final Project project;

    public <Task extends GlobalRunningTask> void register(
            final @NotNull String name,
            final @NotNull Class<Task> runningTask
    ) {
        final TaskProvider<Task> taskProvider = project.getTasks().register(name, runningTask);
        configureTask(taskProvider);
    }

    private <Task extends GlobalRunningTask> void configureTask(final @NotNull TaskProvider<Task> provider) {
        final Task task = provider.get();
        final List<Class<? extends Step>> steps = task.steps();

        final StepContext stepContext = new StepContext(steps, this.project);
        stepContext.setup(task);
    }
}
