package wtf.ranked.hytale.server.runner.task.global;

import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.step.TaskStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadModStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadServerAssetStep;
import wtf.ranked.hytale.server.runner.step.impl.PrepareDownloaderStep;
import wtf.ranked.hytale.server.runner.step.impl.PrepareReinstallStep;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;

import java.util.List;

/**
 * Task for performing a clean update of the Hytale server environment.
 * <p>
 * Unlike the launch task, this orchestrator begins by purging existing
 * server assets and configuration to ensure a fresh installation before
 * re-downloading the core software and mods.
 */
public abstract class UpdateServerTask extends GlobalRunningTask {

    /**
     * Defines the sequential update pipeline.
     *
     * @return an ordered list of steps including environment purging
     * and dependency re-acquisition.
     */
    @Override
    public @NonNull List<Class<? extends TaskStep>> steps() {
        return List.of(
                PrepareReinstallStep.class,
                PrepareDownloaderStep.class,
                DownloadServerAssetStep.class,
                DownloadModStep.class
        );
    }
}
