package wtf.ranked.hytale.server.runner.step.impl;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.workers.WorkerExecutor;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.mod.Mod;
import wtf.ranked.hytale.server.runner.mod.ModDownloader;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;

import javax.inject.Inject;

/**
 * Step responsible for resolving and downloading all configured server mods.
 * <p>
 * This step iterates through the declared mod list and fetches them from their
 * respective remote sources into the project's mod directory.
 */
@NullMarked
public abstract class DownloadModStep extends TaskStepDefault {

    public DownloadModStep() {
        getMods().convention(getHytalePluginExtension().getModExtension().getMods());
        getModDirectory().convention(getHytalePluginExtension().getModDirectory());
    }


    @Override
    public void runStep() {
        final ModDownloader modContext = new ModDownloader(getHytalePluginExtension(), getWorkerExecutor());
        final ListProperty<Mod> mods = getMods();
        modContext.downloadAllMods(mods.get());
    }

    @Input
    protected abstract ListProperty<Mod> getMods();

    @OutputDirectory
    protected abstract DirectoryProperty getModDirectory();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();
}