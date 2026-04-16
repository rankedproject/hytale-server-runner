package net.rankedproject.hytale.boot.mod.type;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public final class GithubMod extends UrlMod {

    private static final String GITHUB_URL = "https://github.com/%s/%s/releases/download/%s/%s";

    private GithubMod(final @NotNull String fileName, final @NotNull URI uri) {
        super(fileName, uri);
    }

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