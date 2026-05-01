package wtf.ranked.hytale.server.runner.step.impl;

import org.gradle.api.provider.ListProperty;
import org.gradle.workers.WorkerExecutor;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.mod.Mod;
import wtf.ranked.hytale.server.runner.mod.ModDownloader;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;

import javax.inject.Inject;

/**
 * Step that resolves and downloads all configured mods.
 * <p>
 * mod dependencies from their respective sources.
 */
public abstract class DownloadModStep extends TaskStepDefault {

    @Override
    public void runStep() {
        final ModDownloader modContext = new ModDownloader(getHytalePluginExtension(), getWorkerExecutor());
        final ListProperty<Mod> mods = getHytalePluginExtension().getModExtension().getMods();
        modContext.downloadAllMods(mods.get());
    }

    @Inject
    protected abstract @NonNull WorkerExecutor getWorkerExecutor();
}