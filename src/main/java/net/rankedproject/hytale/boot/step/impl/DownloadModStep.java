package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.mod.Mod;
import net.rankedproject.hytale.boot.mod.ModDownloader;
import net.rankedproject.hytale.boot.mod.registry.ModDownloaderStrategyRegistry;
import net.rankedproject.hytale.boot.step.TaskStep;
import net.rankedproject.hytale.boot.step.type.TaskStepDefault;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jetbrains.annotations.NotNull;

/**
 * Step that resolves and downloads all configured mods.
 * <p>
 * Utilizes the {@link ModDownloaderStrategyRegistry} to fetch
 * mod dependencies from their respective sources.
 */
public abstract class DownloadModStep extends TaskStepDefault {

    @Override
    public final @NotNull TaskStep.Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    private void startStep() {
        final ModDownloader modContext = new ModDownloader(getDownloaderStrategyRegistry());
        final ListProperty<Mod> mods = getHytaleBootExtension().getModExtension().getMods();
        modContext.download(mods.get());
    }

    @ServiceReference("modDownloaderStrategyRegistry")
    protected abstract @NotNull Property<ModDownloaderStrategyRegistry> getDownloaderStrategyRegistry();
}
