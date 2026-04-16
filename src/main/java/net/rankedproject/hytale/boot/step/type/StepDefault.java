package net.rankedproject.hytale.boot.step.type;

import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.task.type.InternalRunningTask;

/**
 * A step implementation for standard internal logic.
 * <p>
 * Combines the {@link Step} lifecycle with a {@code DefaultTask}.
 * Use this for workers that perform logic within the Gradle JVM,
 * such as file management, asset downloading, or registry preparation.
 */
public abstract class StepDefault extends InternalRunningTask.InternalDefaultRunningTask implements Step {

    @Override
    public final void run() {
        runStep();
    }
}
