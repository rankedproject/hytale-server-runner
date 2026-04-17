package net.rankedproject.hytale.boot.mod.type;

import lombok.Getter;
import net.rankedproject.hytale.boot.mod.Mod;
import org.jspecify.annotations.NonNull;

import java.net.URI;

/**
 * A mod implementation defined by a direct web URL.
 * <p>
 * This type is used for mods hosted on static file servers or CDNs
 * where a direct link to the {@code .jar} file is available.
 */
@Getter
public class UrlMod extends Mod {

    /**
     * The source location of the mod file.
     */
    private final URI uri;

    /**
     * Internal constructor for creating a URL-based mod.
     *
     * @param fileName normalized name of the file to save
     * @param uri      source location
     */
    protected UrlMod(final @NonNull String fileName, final @NonNull URI uri) {
        super(fileName);
        this.uri = uri;
    }

    /**
     * Static factory to create a mod from a URL string.
     *
     * @param url      the raw string URL
     * @param fileName the desired local file name
     * @return a new UrlMod instance
     */
    public static @NonNull UrlMod of(final @NonNull String url, final @NonNull String fileName) {
        return new UrlMod(Mod.normalizeFileName(fileName), URI.create(url));
    }

    /**
     * Static factory to create a mod from a {@link URI}.
     *
     * @param uri      the source URI
     * @param fileName the desired local file name
     * @return a new UrlMod instance
     */
    public static @NonNull UrlMod of(final @NonNull URI uri, final @NonNull String fileName) {
        return new UrlMod(Mod.normalizeFileName(fileName), uri);
    }
}
