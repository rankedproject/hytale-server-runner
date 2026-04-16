package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.mod.Mod;
import net.rankedproject.hytale.boot.mod.ModContext;
import net.rankedproject.hytale.boot.mod.registry.ModDownloaderStrategyRegistry;
import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.type.StepDefault;
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
public abstract class DownloadModStep extends StepDefault {

    @Override
    public @NotNull Step.Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    public void startStep() {
        final ModContext modContext = new ModContext(getDownloaderStrategyRegistry());
        final ListProperty<Mod> mods = getHytaleBootExtension().getModExtension().getMods();
        modContext.download(mods.get());
    }

    @ServiceReference("modDownloaderStrategyRegistry")
    protected abstract @NotNull Property<ModDownloaderStrategyRegistry> getDownloaderStrategyRegistry();
}
