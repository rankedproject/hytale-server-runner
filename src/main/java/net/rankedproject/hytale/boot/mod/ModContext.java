package net.rankedproject.hytale.boot.mod;

import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.mod.registry.ModDownloaderStrategyRegistry;
import net.rankedproject.hytale.boot.mod.strategy.ModDownloaderStrategy;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public final class ModContext {

    private final Property<ModDownloaderStrategyRegistry> downloaderStrategyRegistry;

    public void download(final @NotNull List<? extends Mod> mods) {
        mods.forEach(this::downloadMod);
    }

    private <M extends Mod> void downloadMod(final @NotNull M mod) {
        final Class<? extends Mod> modeType = mod.getClass();
        final ModDownloaderStrategyRegistry modDownloaderStrategyRegistry = downloaderStrategyRegistry.get();

        final ModDownloaderStrategy<M> modDownloaderStrategy = modDownloaderStrategyRegistry.getDownloader(modeType);
        modDownloaderStrategy.downloadMod(mod);
    }
}
