package wtf.ranked.hytale.server.runner.step;

import wtf.ranked.hytale.server.runner.task.GradleRunningTask;

public interface TaskStep extends GradleRunningTask {

    /**
     * Standard execution flow for the step.
     * <p>
     * Runs the start logic, followed by the optional stop logic.
     */
    void runStep();
}
