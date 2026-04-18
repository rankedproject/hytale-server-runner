package wtf.ranked.hytale.server.runner.step.type;

import wtf.ranked.hytale.server.runner.step.TaskStep;
import wtf.ranked.hytale.server.runner.task.type.InternalRunningTask;

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
