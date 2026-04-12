package net.cachewrapper.hytale.runner.step;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@RequiredArgsConstructor
public final class StepContext {

    private final Step defaultStep;

    private @Nullable Step currentStep;

    public void switchNextStep() {
        if (this.currentStep == null) {
            activateStep(this.defaultStep);
            return;
        }

        final Step.Options options = currentStep.options(this);
        final Step nextStep = options.nextStep();

        if (nextStep != null) {
            activateStep(nextStep);
        }
    }

    private void activateStep(final @NotNull Step nextStep) {
        if (this.currentStep != null) {
            final Step.Options options = currentStep.options(this);
            final Runnable stopStep = options.stopStep();

            if (stopStep != null) {
                stopStep.run();
            }
        }

        this.currentStep = nextStep;
        final Step.Options nextStepOption = nextStep.options(this);
        nextStepOption.startStep().run();
    }
}