package wtf.ranked.hytale.server.runner.step;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jspecify.annotations.NonNull;

/**
 * Orchestrator that transforms a list of Step classes into a linked Gradle task chain.
 * <p>
 * It registers each {@link TaskStep} as an internal task and uses a reduction
 * algorithm to ensure that each step depends on the one preceding it,
 * effectively creating a sequential execution queue.
 */
@RequiredArgsConstructor
public final class TaskStepLoader {

    private final GlobalRunningTask runningTask;
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
     */
    public void setup() {
        this.runningTask.steps().stream()
                .map(this::register)
                .reduce((previous, current) -> {
                    current.configure(task -> task.dependsOn(previous));
                    return current;
                })
                .ifPresent(this.runningTask::dependsOn);
    }

    private @NonNull TaskProvider<?> register(final @NonNull Class<? extends TaskStep> step) {
        final TaskContainer container = project.getTasks();
        return container.register(TaskLoaderUtil.newTaskIdentifier(this.runningTask, step), step);
    }

    @UtilityClass
    private static final class TaskLoaderUtil {

        private final String STEP_TASK_IDENTIFIER_TEMPLATE = "%s_%s";

        /**
         * Generates a unique task name using the {@link #STEP_TASK_IDENTIFIER_TEMPLATE}.
         * <p>
         * This name is used to register the step as a sub-task of the provided global task.
         *
         * @param runningTask the main task providing the root name
         * @param step        the step class providing the sub-task name (simple class name)
         * @return the formatted task name, e.g., "globalTaskName_StepSimpleName"
         */
        private @NonNull String newTaskIdentifier(
                final @NonNull GlobalRunningTask runningTask,
                final @NonNull Class<? extends TaskStep> step
        ) {
            return STEP_TASK_IDENTIFIER_TEMPLATE.formatted(runningTask.getName(), step.getSimpleName());
        }
    }
}
