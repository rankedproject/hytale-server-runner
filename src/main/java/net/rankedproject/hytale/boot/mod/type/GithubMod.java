package net.rankedproject.hytale.boot.mod.type;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * A specialized mod implementation for files hosted on GitHub Releases.
 * <p>
 * This class acts as a template that automatically constructs a direct
 * download URL based on the repository owner, name, release tag, and asset name.
 */
public final class GithubMod extends UrlMod {

    private static final String GITHUB_URL = "https://github.com/%s/%s/releases/download/%s/%s";

    private GithubMod(final @NotNull String fileName, final @NotNull URI uri) {
        super(fileName, uri);
    }

    /**
     * Static factory to build a GithubMod from repository coordinates.
     *
     * @param owner      the GitHub user or organization
     * @param repository the name of the repository
     * @param tag        the release tag (e.g., "v1.0.0")
     * @param assetName  the specific file name attached to the release
     * @return a new GithubMod with a formatted download URI
     */
    public static @NotNull GithubMod of(
            final @NotNull String owner,
            final @NotNull String repository,
            final @NotNull String tag,
            final @NotNull String assetName
    ) {
        final String url = GITHUB_URL.formatted(owner, repository, tag, assetName);
        return new GithubMod(assetName, URI.create(url));
    }
}