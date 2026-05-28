package wtf.ranked.hytale.server.runner.task.global;

import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.step.TaskStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadModStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadServerAssetStep;
import wtf.ranked.hytale.server.runner.step.impl.LaunchServerStep;
import wtf.ranked.hytale.server.runner.step.impl.PrepareDownloaderStep;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;

import java.util.List;

/**
 * The primary execution task for booting a Hytale server.
 * <p>
 * This task acts as a global orchestrator for the server lifecycle. It defines
 * and executes a multistep pipeline that ensures the environment is ready,
 * dependencies such as assets and mods are resolved, and the server process
 * is correctly launched.
 */
public abstract class LaunchServerTask extends GlobalRunningTask {

    /**
     * Defines the sequential boot pipeline for the server.
     * <p>
     * The returned list specifies the exact order of execution for the lifecycle steps,
     * starting from environment preparation to final server execution.
     *
     * @return an ordered list of {@link TaskStep} classes representing the boot sequence.
     */
    @Override
    public @NonNull List<Class<? extends TaskStep>> steps() {
        return List.of(
                PrepareDownloaderStep.class,
                DownloadServerAssetStep.class,
                DownloadModStep.class,
                LaunchServerStep.class
        );
    }
}