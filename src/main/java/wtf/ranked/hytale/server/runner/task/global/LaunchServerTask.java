package wtf.ranked.hytale.server.runner.task.global;

import wtf.ranked.hytale.server.runner.step.TaskStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadModStep;
import wtf.ranked.hytale.server.runner.step.impl.DownloadServerAssetStep;
import wtf.ranked.hytale.server.runner.step.impl.LaunchServerStep;
import wtf.ranked.hytale.server.runner.step.impl.PrepareDownloaderStep;
import wtf.ranked.hytale.server.runner.task.type.GlobalRunningTask;
import org.jspecify.annotations.NonNull;

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
    public @NonNull List<Class<? extends TaskStep>> steps() {
        return List.of(
                PrepareDownloaderStep.class,
                DownloadServerAssetStep.class,
                DownloadModStep.class,
                LaunchServerStep.class
        );
    }
}
