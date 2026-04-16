package net.rankedproject.hytale.boot.mod;

import net.rankedproject.hytale.boot.mod.type.GithubMod;
import net.rankedproject.hytale.boot.mod.type.UrlMod;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.jetbrains.annotations.NotNull;

/**
 * Configuration extension for managing Hytale mods.
 * <p>
 * Provides a DSL for users to define mod dependencies from various
 * sources like direct URLs or GitHub releases. These definitions are
 * collected into a list and processed by the {@code DownloadModStep}.
 */
@SuppressWarnings("unused")
public abstract class ModExtension {

    /**
     * Retrieves the list of all configured mods.
     *
     * @return a property containing the collection of {@link Mod} instances
     */
    @Input
    public abstract @NotNull ListProperty<Mod> getMods();

    /**
     * Adds a mod to the configuration via a direct download link.
     *
     * @param url  the direct download URL for the mod file
     * @param file the name the file should be saved as locally
     */
    public void url(final @NotNull String url, final @NotNull String file) {
        final UrlMod mod = UrlMod.of(url, file);
        getMods().add(mod);
    }

    /**
     * Adds a mod to the configuration from a GitHub Release asset.
     *
     * @param owner      the GitHub user or organization
     * @param repository the name of the repository
     * @param tag        the specific release tag (e.g., "v1.0.0")
     * @param assetName  the name of the file attached to the release
     */
    public void github(
            final @NotNull String owner,
            final @NotNull String repository,
            final @NotNull String tag,
            final @NotNull String assetName
    ) {
        final GithubMod mod = GithubMod.of(owner, repository, tag, assetName);
        getMods().add(mod);
    }
}