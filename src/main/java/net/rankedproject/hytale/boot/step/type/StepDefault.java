package net.rankedproject.hytale.boot.step.type;

import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.task.type.InternalRunningTask;

public abstract class StepDefault extends InternalRunningTask.InternalDefaultRunningTask implements Step {

    @Override
    public final void run() {
        runStep();
    }
}
