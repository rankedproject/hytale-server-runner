package net.cachewrapper.hytale.runner.step;

import lombok.Builder;
import net.cachewrapper.hytale.runner.HytaleRunnerPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public interface Step {

    Logger LOGGER = HytaleRunnerPlugin.LOGGER;

    @NotNull Step.Options options(@NotNull StepContext stepContext);

    @Builder
    record Options(
            @NotNull Runnable startStep,
            @Nullable Runnable stopStep,
            @Nullable Step nextStep
    ) {}
}
