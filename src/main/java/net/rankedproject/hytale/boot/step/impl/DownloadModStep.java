package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.mod.Mod;
import net.rankedproject.hytale.boot.mod.ModDownloader;
import net.rankedproject.hytale.boot.step.type.TaskStepDefault;
import org.gradle.api.provider.ListProperty;
import org.gradle.workers.WorkerExecutor;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Step that resolves and downloads all configured mods.
 * <p>
 * mod dependencies from their respective sources.
 */
public abstract class DownloadModStep extends TaskStepDefault {

    @Override
    public final @NotNull Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    private void startStep() {
        final ModDownloader modContext = new ModDownloader(getHytaleBootExtension(), getWorkerExecutor());
        final ListProperty<Mod> mods = getHytaleBootExtension().getModExtension().getMods();
        modContext.download(mods.get());
    }

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();
}
