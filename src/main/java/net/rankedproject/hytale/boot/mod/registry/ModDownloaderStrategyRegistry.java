package net.rankedproject.hytale.boot.mod.registry;

import net.rankedproject.hytale.boot.extension.HytaleExtensionParameters;
import net.rankedproject.hytale.boot.mod.Mod;
import net.rankedproject.hytale.boot.mod.strategy.ModDownloaderStrategy;
import net.rankedproject.hytale.boot.mod.strategy.UrlDownloaderStrategy;
import net.rankedproject.hytale.boot.mod.type.GithubMod;
import net.rankedproject.hytale.boot.mod.type.UrlMod;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.ServiceReference;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry service that manages the mapping between mod types and their download strategies.
 * <p>
 * This service acts as a central hub for resolving the correct {@link ModDownloaderStrategy}
 * for any given {@link Mod} implementation. It is pre-configured with default strategies
 * for URL and GitHub-based mods.
 */
public abstract class ModDownloaderStrategyRegistry implements BuildService<HytaleExtensionParameters> {

    private final Map<Class<? extends Mod>, ModDownloaderStrategy<?>> downloaderStrategy = new HashMap<>();

    /**
     * Constructs a new registry and initializes default strategy mappings.
     */
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

    /**
     * Retrieves the strategy associated with a specific mod implementation.
     *
     * @param modType the class of the mod being processed
     * @param <M>     the specific mod type
     * @return the downloader strategy capable of handling the specified mod type
     * @throws NullPointerException if no strategy is registered for the given type
     */
    @SuppressWarnings("unchecked")
    public final <M extends Mod> @NotNull ModDownloaderStrategy<M> getDownloader(
            final @NotNull Class<? extends Mod> modType
    ) {
        return (ModDownloaderStrategy<M>) this.downloaderStrategy.get(modType);
    }

    @ServiceReference("urlDownloaderStrategy")
    protected abstract @NotNull Property<UrlDownloaderStrategy> getUrlDownloaderStrategy();
}
