package wtf.ranked.hytale.server.runner.mod;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.mod.type.GithubMod;
import wtf.ranked.hytale.server.runner.mod.type.UrlMod;

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
    public abstract @NonNull ListProperty<Mod> getMods();

    /**
     * Adds a mod to the configuration via a direct download link.
     *
     * @param url  the direct download URL for the mod file
     * @param file the name the file should be saved as locally
     */
    public final void url(final @NonNull String url, final @NonNull String file) {
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
    public final void github(
            final @NonNull String owner,
            final @NonNull String repository,
            final @NonNull String tag,
            final @NonNull String assetName
    ) {
        final GithubMod mod = GithubMod.of(owner, repository, tag, assetName);
        getMods().add(mod);
    }
}
