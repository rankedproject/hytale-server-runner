package net.rankedproject.hytale.boot.mod;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Abstract base for all mod definitions.
 * <p>
 * This class serves as a blueprint for mod metadata. It is {@link Serializable}
 * to allow Gradle to cache and transport mod definitions between different
 * worker processes or build phases.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Mod implements Serializable {

    /**
     * The target name for the mod file on the local disk.
     */
    protected final String fileName;

    /**
     * Ensures the provided file name has a consistent {@code .jar} extension.
     *
     * @param fileName the raw file name string
     * @return the normalized file name ending in ".jar"
     */
    protected static @NotNull String normalizeFileName(final @NotNull String fileName) {
        return fileName.endsWith(".jar") ? fileName : fileName + ".jar";
    }
}