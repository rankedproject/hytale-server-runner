package net.rankedproject.hytale.boot.step.type;

import net.rankedproject.hytale.boot.step.TaskStep;
import net.rankedproject.hytale.boot.task.type.InternalRunningTask;

/**
 * A step implementation for external process execution.
 * <p>
 * Combines the {@link TaskStep} lifecycle with {@code JavaExec} capabilities.
 * Use this for steps that must run as a standalone Java process, such as
 * the final server boot.
 */
public abstract class TaskStepExec extends InternalRunningTask.InternalExecRunningTask implements TaskStep {

    @Override
    public final void run() {
        runStep();
    }
}
