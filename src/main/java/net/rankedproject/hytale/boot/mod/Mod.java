package net.rankedproject.hytale.boot.mod;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Mod {

    protected final String fileName;

    protected static @NotNull String normalizeFileName(final @NotNull String fileName) {
        return fileName.endsWith(".jar") ? fileName : fileName + ".jar";
    }
}
