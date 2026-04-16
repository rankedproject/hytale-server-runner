package net.rankedproject.hytale.boot.mod;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.mod.registry.ModDownloaderStrategyRegistry;
import net.rankedproject.hytale.boot.mod.strategy.ModDownloaderStrategy;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Execution context for processing mod downloads.
 * <p>
 * This class acts as a mediator between the configured mod list and
 * the available download strategies. It iterates through the mods and
 * dispatches each to its appropriate {@link ModDownloaderStrategy}
 * based on the mod's implementation type.
 */
@RequiredArgsConstructor
public final class ModDownloader {

    private final Property<ModDownloaderStrategyRegistry> downloaderStrategyRegistry;

    /**
     * Iterates through a list of mods and triggers the download process for each.
     *
     * @param mods the collection of mods defined in the build script
     */
    public void download(final @NotNull List<? extends Mod> mods) {
        mods.forEach(this::downloadMod);
    }

    /**
     * Resolves the correct strategy for a specific mod type and executes the download.
     *
     * @param mod the mod instance to process
     * @param <M> the specific implementation type of the Mod
     */
    private <M extends Mod> void downloadMod(final @NotNull M mod) {
        final Class<? extends Mod> modeType = mod.getClass();
        final ModDownloaderStrategyRegistry modDownloaderStrategyRegistry = downloaderStrategyRegistry.get();

        final ModDownloaderStrategy<M> modDownloaderStrategy = modDownloaderStrategyRegistry.getDownloader(modeType);
        modDownloaderStrategy.downloadMod(mod);
    }
}
