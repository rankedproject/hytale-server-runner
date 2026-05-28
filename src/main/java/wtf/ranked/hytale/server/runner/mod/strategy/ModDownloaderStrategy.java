package wtf.ranked.hytale.server.runner.mod.strategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildService;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.mod.Mod;
import wtf.ranked.hytale.server.runner.resource.exception.ResourceDownloadException;

/**
 * Base strategy for downloading and installing Hytale mods.
 * <p>
 * As a {@link BuildService}, it has access to global project parameters.
 * It enforces a "check-before-download" workflow to ensure mods are
 * only fetched if they are missing from the local environment.
 *
 * @param <M> the specific type of Mod this strategy handles
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ModDownloaderStrategy<M extends Mod>
        implements WorkAction<ModDownloaderStrategy.ModDownloaderExtension> {

    /**
     * {@inheritDoc}
     * <p>
     * This method is marked final to ensure the "check-before-download"
     * workflow remains consistent across all strategy implementations.
     */
    @Override
    @SuppressWarnings("unchecked")
    public final void execute() throws ResourceDownloadException {
        final M mod = (M) getParameters().getMod().get();
        if (isModInstalled(mod.getFileName())) {
            return;
        }

        download(mod);
    }

    /**
     * Checks if a mod is already present in the local mods directory.
     *
     * @param identifier the file name or identifier of the mod to check
     * @return {@code true} if the mod file exists on disk, {@code false} otherwise
     */
    protected boolean isModInstalled(final @NonNull String identifier) {
        final DirectoryProperty modDirectory = getParameters().getHytalePluginExtension().get().getModDirectory();
        return modDirectory.get().file(identifier).getAsFile().exists();
    }

    /**
     * Implementation-specific logic to acquire the mod file.
     *
     * @param mod the mod definition
     */
    protected abstract void download(@NonNull M mod);

    /**
     * Parameters for the mod downloader work action.
     */
    @NullMarked
    public interface ModDownloaderExtension extends WorkParameters {

        /**
         * The Hytale boot configuration extension.
         *
         * @return the boot extension property
         */
        Property<HytalePluginExtension> getHytalePluginExtension();

        /**
         * The mod instance to be downloaded.
         *
         * @return the mod property
         */
        Property<Mod> getMod();
    }
}
