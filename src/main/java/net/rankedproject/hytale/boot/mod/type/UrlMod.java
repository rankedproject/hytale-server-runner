package net.rankedproject.hytale.boot.mod.type;

import lombok.Getter;
import net.rankedproject.hytale.boot.mod.Mod;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

@Getter
public class UrlMod extends Mod {

    private final URI uri;

    protected UrlMod(final @NotNull String fileName, final @NotNull URI uri) {
        super(fileName);
        this.uri = uri;
    }

    public static @NotNull UrlMod of(final @NotNull String url, final @NotNull String fileName) {
        return new UrlMod(Mod.normalizeFileName(fileName), URI.create(url));
    }

    public static @NotNull UrlMod of(final @NotNull URI uri, final @NotNull String fileName) {
        return new UrlMod(Mod.normalizeFileName(fileName), uri);
    }
}