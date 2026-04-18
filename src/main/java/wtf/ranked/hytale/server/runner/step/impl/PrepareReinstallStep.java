package wtf.ranked.hytale.server.runner.step.impl;

import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;
import wtf.ranked.hytale.server.runner.util.FileUtil;
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
                .startStep(() -> FileUtil.deleteDirectory(getHytalePluginExtension().getRunDirectory().get().getAsFile()))
                .build();
    }
}
