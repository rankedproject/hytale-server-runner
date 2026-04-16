package net.rankedproject.hytale.boot.task.global;

import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.impl.DownloadModStep;
import net.rankedproject.hytale.boot.step.impl.DownloadServerAssetStep;
import net.rankedproject.hytale.boot.step.impl.LaunchServerStep;
import net.rankedproject.hytale.boot.step.impl.PrepareDownloaderStep;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The primary execution task for booting a Hytale server.
 * <p>
 * This task orchestrates a multi-step pipeline that prepares the
 * environment, verifies and downloads missing dependencies (assets and mods),
 * and finally executes the server process.
 */
public abstract class LaunchServerTask extends GlobalRunningTask {

    /**
     * Defines the sequential boot pipeline.
     *
     * @return an ordered list of steps required to reach a running server state.
     */
    @Override
    public @NotNull List<Class<? extends Step>> steps() {
        return List.of(
                PrepareDownloaderStep.class,
                DownloadServerAssetStep.class,
                DownloadModStep.class,
                LaunchServerStep.class
        );
    }
}