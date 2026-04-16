package net.rankedproject.hytale.boot.task.global;

import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.impl.DownloadModStep;
import net.rankedproject.hytale.boot.step.impl.DownloadServerAssetStep;
import net.rankedproject.hytale.boot.step.impl.LaunchServerStep;
import net.rankedproject.hytale.boot.step.impl.PrepareDownloaderStep;
import net.rankedproject.hytale.boot.task.type.GlobalRunningTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class LaunchServerTask extends GlobalRunningTask {

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
