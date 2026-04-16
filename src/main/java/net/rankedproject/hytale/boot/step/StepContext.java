package net.rankedproject.hytale.boot.step;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public final class StepContext {

    private final List<Class<? extends Step>> steps;
    private final Project project;

    public void setup(final @NotNull GlobalRunningTask globalRunningTask) {
        final TaskContainer container = project.getTasks();
        this.steps.stream()
                .map(stepClass -> container.register(globalRunningTask.getName() + stepClass.getSimpleName(), stepClass))
                .reduce((previous, current) -> {
                    current.configure(task -> task.dependsOn(previous));
                    return current;
                })
                .ifPresent(globalRunningTask::dependsOn);
    }
}