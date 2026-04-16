package net.rankedproject.hytale.boot.step;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Orchestrator that transforms a list of Step classes into a linked Gradle task chain.
 * <p>
 * It registers each {@link Step} as an internal task and uses a reduction
 * algorithm to ensure that each step depends on the one preceding it,
 * effectively creating a sequential execution queue.
 */
@RequiredArgsConstructor
public final class StepContext {

    private final List<Class<? extends Step>> steps;
    private final Project project;

    /**
     * Configures the task dependencies for a given Global task.
     * <p>
     * This method performs the following:
     * <ol>
     * <li>Registers each step as a unique task name (GlobalTask + StepName).</li>
     * <li>Links tasks: Step B dependsOn Step A.</li>
     * <li>Makes the Global task depend on the final step in the chain.</li>
     * </ol>
     *
     * @param globalRunningTask the main entry-point task to be configured
     */
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