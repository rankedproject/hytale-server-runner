package net.rankedproject.hytale.boot.mod.registry;

import net.rankedproject.hytale.boot.mod.Mod;
import net.rankedproject.hytale.boot.mod.strategy.ModDownloaderStrategy;
import net.rankedproject.hytale.boot.mod.strategy.UrlDownloaderStrategy;
import net.rankedproject.hytale.boot.mod.type.GithubMod;
import net.rankedproject.hytale.boot.mod.type.UrlMod;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.gradle.api.services.ServiceReference;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public abstract class ModDownloaderStrategyRegistry implements BuildService<BuildServiceParameters.None> {

    private final Map<Class<? extends Mod>, ModDownloaderStrategy<?>> downloaderStrategy = new HashMap<>();

    @Inject
    public ModDownloaderStrategyRegistry() {
        registerDefaults();
    }

    private void registerDefaults() {
        register(UrlMod.class, getUrlDownloaderStrategy().get());
        register(GithubMod.class, getUrlDownloaderStrategy().get());
    }

    private void register(
            final @NotNull Class<? extends Mod> modType,
            final @NotNull ModDownloaderStrategy<?> downloaderStrategy
    ) {
        this.downloaderStrategy.put(modType, downloaderStrategy);
    }

    @SuppressWarnings("unchecked")
    public  <M extends Mod> @NotNull ModDownloaderStrategy<M> getDownloader(
            final @NotNull Class<? extends Mod> modType
    ) {
        return (ModDownloaderStrategy<M>) this.downloaderStrategy.get(modType);
    }

    @ServiceReference("urlDownloaderStrategy")
    protected abstract @NotNull Property<UrlDownloaderStrategy> getUrlDownloaderStrategy();
}
