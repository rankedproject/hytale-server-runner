package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.type.StepDefault;
import net.rankedproject.hytale.boot.util.FileUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Step that wipes the current server installation.
 * <p>
 * Used primarily by {@code UpdateServerTask} to ensure a clean slate
 * by deleting all files in the configured run directory.
 */
public abstract class PrepareReinstallStep extends StepDefault {

    @Override
    public @NotNull Step.Options options() {
        return Options.builder()
                .startStep(() -> FileUtil.deleteFiles(getHytaleBootExtension().getRunDirectory().get().getAsFile()))
                .build();
    }
}
