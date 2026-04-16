package net.rankedproject.hytale.boot.step.type;

import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.task.type.InternalRunningTask;

public abstract class StepExec extends InternalRunningTask.InternalExecRunningTask implements Step {

    @Override
    public final void run() {
        runStep();
    }
}
