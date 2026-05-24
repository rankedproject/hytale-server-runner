package wtf.ranked.hytale.server.runner.step;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;

import java.util.List;

/**
 * Orchestrator that transforms a list of Step classes into a linked Gradle task chain.
 * <p>
 * It registers each {@link TaskStep} as an internal task and uses a reduction
 * algorithm to ensure that each step depends on the one preceding it,
 * effectively creating a sequential execution queue.
 */
@NullMarked
@RequiredArgsConstructor
public final class TaskStepLoader {

    private final Project project;
    private final HytalePluginExtension pluginExtension;

    /**
     * Configures the task dependencies for a given Global task to create a sequential execution pipeline.
     * <p>
     * This method:
     * <ol>
     * <li>Resolves all build dependencies and internal steps into a single ordered list.</li>
     * <li>Links the task chain so that each step depends on the preceding one (Step A -> Step B).</li>
     * <li>Ensures the {@code GlobalRunningTask} executes only after the entire chain completes.</li>
     * </ol>
     */
    public void setup(final GlobalRunningTask runningTask) {
        final TaskContainer container = project.getTasks();
        final List<TaskProvider<Task>> dependsOn = pluginExtension.getDependsOn().get().stream()
                .map(container::named)
                .toList();

        final List<? extends TaskProvider<?>> steps = runningTask.steps().stream()
                .map(TaskStepRegistry::getName)
                .map(container::named)
                .toList();

        final List<TaskProvider<?>> mergeSteps = ImmutableList.<TaskProvider<?>>builder()
                .addAll(dependsOn)
                .addAll(steps)
                .build();

        mergeSteps.stream()
                .reduce((previous, current) -> {
                    runningTask.dependsOn(previous);
                    return current;
                })
                .ifPresent(runningTask::dependsOn);
    }
}
