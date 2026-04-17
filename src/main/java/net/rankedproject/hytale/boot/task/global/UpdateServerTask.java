package net.rankedproject.hytale.boot.task.global;

import net.rankedproject.hytale.boot.step.TaskStep;
import net.rankedproject.hytale.boot.step.impl.DownloadModStep;
import net.rankedproject.hytale.boot.step.impl.DownloadServerAssetStep;
import net.rankedproject.hytale.boot.step.impl.PrepareDownloaderStep;
import net.rankedproject.hytale.boot.step.impl.PrepareReinstallStep;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.jspecify.annotations.NonNull;

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
