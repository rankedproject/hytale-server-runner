package net.rankedproject.hytale.boot.mod.strategy;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.extension.HytaleExtensionParameters;
import net.rankedproject.hytale.boot.mod.Mod;
import org.gradle.api.services.BuildService;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
public abstract class ModDownloaderStrategy<M extends Mod> implements BuildService<HytaleExtensionParameters> {

    /**
     * Orchestrates the installation of a mod.
     * Checks for the mod's existence on disk before triggering
     * the implementation-specific {@link #download(Mod)} method.
     *
     * @param mod the mod definition to process
     */
    public final void downloadMod(final @NotNull M mod) {
        final HytaleBootExtension hytaleBootExtension = this.getParameters().getHytaleBootExtension().get();
        final File modsDirectory = hytaleBootExtension.getModDirectory().get().getAsFile();

        if (!modsDirectory.exists()) {
            modsDirectory.mkdirs();
        }

        if (this.isModInstalled(mod.getFileName())) {
            return;
        }

        this.download(mod);
    }

    /**
     * Checks if a mod is already present in the local mods directory.
     *
     * @param identifier the file name or identifier of the mod to check
     * @return {@code true} if the mod file exists on disk, {@code false} otherwise
     */
    protected boolean isModInstalled(final @NotNull String identifier) {
        final HytaleBootExtension hytaleBootExtension = this.getParameters().getHytaleBootExtension().get();
        final File modsDirectory = hytaleBootExtension.getModDirectory().get().getAsFile();

        return new File(modsDirectory, identifier).exists();
    }

    /**
     * Implementation-specific logic to acquire the mod file.
     *
     * @param mod the mod definition
     */
    protected abstract void download(@NotNull M mod);
}
