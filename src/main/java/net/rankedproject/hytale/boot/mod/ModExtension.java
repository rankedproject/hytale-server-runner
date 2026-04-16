package net.rankedproject.hytale.boot.mod;

import net.rankedproject.hytale.boot.mod.type.GithubMod;
import net.rankedproject.hytale.boot.mod.type.UrlMod;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class ModExtension {

    @Input
    public abstract @NotNull ListProperty<Mod> getMods();

    public void url(final @NotNull String url, final @NotNull String file) {
        final UrlMod mod = UrlMod.of(url, file);
        getMods().add(mod);
    }

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