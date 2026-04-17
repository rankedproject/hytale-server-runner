package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.step.type.TaskStepDefault;
import net.rankedproject.hytale.boot.util.FileUtil;
import org.jspecify.annotations.NonNull;

/**
 * Step that wipes the current server installation.
 * <p>
 * Used primarily by {@code UpdateServerTask} to ensure a clean slate
 * by deleting all files in the configured run directory.
 */
public abstract class PrepareReinstallStep extends TaskStepDefault {

    @Override
    public final @NonNull Options options() {
        return Options.builder()
                .startStep(() -> FileUtil.deleteDirectory(getHytaleBootExtension().getRunDirectory().get().getAsFile()))
                .build();
    }
}
