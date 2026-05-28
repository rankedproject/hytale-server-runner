package wtf.ranked.hytale.server.runner.step;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskProvider;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;

import java.util.List;

/**
 * Orchestrator that transforms a sequence of {@link TaskStep} classes into a linked Gradle task chain.
 * <p>
 * This loader implements a sequential execution pipeline by configuring task dependencies.
 * It ensures that build prerequisites are satisfied before step execution, and that
 * individual steps are run in the precise order defined by the {@link GlobalRunningTask}.
 */
@NullMarked
@RequiredArgsConstructor
public final class TaskStepLoader {

    private final Project project;
    private final HytalePluginExtension pluginExtension;

    /**
     * Configures the task dependencies for a given {@link GlobalRunningTask} to create
     * a sequential execution pipeline.
     * <p>
     * The method first collects prerequisite build tasks from the {@link HytalePluginExtension}
     * and maps {@link TaskStep} classes into {@link TaskProvider} instances. These are
     * combined into a single flat list.
     * <p>
     * Using a reduction algorithm, the method links these tasks sequentially: each task is
     * configured to depend on the one immediately preceding it in the list via
     * {@code current.dependsOn(previous)}. Finally, the {@code GlobalRunningTask} itself
     * is configured to depend on the last task in this chain.
     *
     * @param runningTask the global task orchestrating the lifecycle
     */
    public void setup(final GlobalRunningTask runningTask) {
        final List<TaskProvider<Task>> dependsOn = pluginExtension.getDependsOn().get().stream()
                .map(identifier -> project.getTasks().named(identifier))
                .toList();

        final List<? extends TaskProvider<?>> steps = runningTask.steps().stream()
                .map(TaskStepRegistry::getName)
                .map(identifier -> project.getTasks().named(identifier))
                .toList();

        final List<TaskProvider<?>> mergeSteps = ImmutableList.<TaskProvider<?>>builder()
                .addAll(dependsOn)
                .addAll(steps)
                .build();

        mergeSteps.stream()
                .reduce((previous, current) -> {
                    current.configure(task -> task.dependsOn(previous));
                    return current;
                })
                .ifPresent(runningTask::dependsOn);
    }
}