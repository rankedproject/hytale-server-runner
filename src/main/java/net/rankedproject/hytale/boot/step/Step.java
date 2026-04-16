package net.rankedproject.hytale.boot.step;

import lombok.Builder;
import org.gradle.api.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Step extends Task {

    default void runStep() {
        final Step.Options options = options();
        options.startStep.run();

        final Runnable stopStep = options.stopStep;
        if (stopStep != null) {
            stopStep.run();
        }
    }

    @NotNull Step.Options options();

    @Builder
    record Options(
            @NotNull Runnable startStep,
            @Nullable Runnable stopStep
    ) {
    }
}
