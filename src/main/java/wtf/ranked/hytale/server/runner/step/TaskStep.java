package wtf.ranked.hytale.server.runner.step;

import lombok.Builder;
import wtf.ranked.hytale.server.runner.task.GradleRunningTask;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents an atomic unit of work within a boot lifecycle.
 * <p>
 * Steps wrap logic into a {@link Options} record, allowing for
 * standardized execution of setup ("start") and cleanup ("stop") logic.
 */
public interface TaskStep extends GradleRunningTask {

    /**
     * Standard execution flow for the step.
     * <p>
     * Runs the start logic, followed by the optional stop logic.
     */
    default void runStep() {
        final TaskStep.Options options = options();
        options.startStep.run();

        final Runnable stopStep = options.stopStep;
        if (stopStep != null) {
            stopStep.run();
        }
    }

    /**
     * @return the configuration options for this step's execution
     */
    @NonNull Options options();

    /**
     * Configuration for step execution.
     *
     * @param startStep the primary logic to execute
     * @param stopStep  optional logic to execute after the startStep (cleanup)
     */
    @Builder
    record Options(
            @NonNull Runnable startStep,
            @Nullable Runnable stopStep
    ) {
    }
}
