package wtf.ranked.hytale.server.runner.step.type;

import wtf.ranked.hytale.server.runner.step.TaskStep;
import wtf.ranked.hytale.server.runner.task.type.InternalRunningTask;

/**
 * A step implementation for standard internal logic.
 * <p>
 * Combines the {@link TaskStep} lifecycle with a {@code DefaultTask}.
 * Use this for workers that perform logic within the Gradle JVM,
 * such as file management, asset downloading, or registry preparation.
 */
public abstract class TaskStepDefault extends InternalRunningTask.InternalDefaultRunningTask implements TaskStep {

    @Override
    public final void run() {
        runStep();
    }
}
