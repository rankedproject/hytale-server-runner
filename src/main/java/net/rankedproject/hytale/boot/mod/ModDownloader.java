package net.rankedproject.hytale.boot.mod;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.mod.strategy.ModDownloaderStrategy;
import net.rankedproject.hytale.boot.mod.strategy.UrlDownloaderStrategy;
import net.rankedproject.hytale.boot.mod.type.GithubMod;
import net.rankedproject.hytale.boot.mod.type.UrlMod;
import org.gradle.workers.WorkQueue;
import org.gradle.workers.WorkerExecutor;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final HytaleBootExtension hytaleBootExtension;
    private final WorkerExecutor workerExecutor;

    /**
     * Iterates through a list of mods and triggers the download process for each.
     *
     * @param mods the collection of mods defined in the build script
     */
    public void download(final @NonNull List<? extends Mod> mods) {
        mods.forEach(this::downloadMod);
    }

    /**
     * Resolves the correct strategy for a specific mod type and executes the download.
     *
     * @param mod the mod instance to process
     * @param <M> the specific implementation type of the Mod
     */
    private <M extends Mod> void downloadMod(final @NonNull M mod) {
        final Class<? extends Mod> modeType = mod.getClass();
        final Class<? extends ModDownloaderStrategy<M>> downloader = ModDownloaderStrategyRegistry.getDownloader(modeType);

        final WorkQueue workQueue = workerExecutor.noIsolation();
        workQueue.submit(downloader, parameters -> {
            parameters.getHytaleBootExtension().set(this.hytaleBootExtension);
            parameters.getMod().set(mod);
        });
    }

    /**
     * Registry service that manages the mapping between mod types and their download strategies.
     * <p>
     * This service acts as a central hub for resolving the correct {@link ModDownloaderStrategy}
     * for any given {@link Mod} implementation. It is pre-configured with default strategies
     * for URL and GitHub-based mods.
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private abstract static class ModDownloaderStrategyRegistry {

        private static final Map<Class<? extends Mod>, Class<? extends ModDownloaderStrategy<?>>> DOWNLOADER_STRATEGY;

        static {
            DOWNLOADER_STRATEGY = new HashMap<>();
            DOWNLOADER_STRATEGY.put(UrlMod.class, UrlDownloaderStrategy.class);
            DOWNLOADER_STRATEGY.put(GithubMod.class, UrlDownloaderStrategy.class);
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
        public static <M extends Mod> @NonNull Class<? extends ModDownloaderStrategy<M>> getDownloader(
                final @NonNull Class<? extends Mod> modType
        ) {
            return (Class<? extends ModDownloaderStrategy<M>>) DOWNLOADER_STRATEGY.get(modType);
        }
    }

}
