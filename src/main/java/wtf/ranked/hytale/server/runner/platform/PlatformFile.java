package wtf.ranked.hytale.server.runner.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;

/**
 * Supported operating system platforms and their corresponding downloader executable filenames.
 */
@Getter
@NullMarked
@RequiredArgsConstructor
public enum PlatformFile {

    WINDOWS("hytale-downloader-windows-amd64.exe"),
    LINUX("hytale-downloader-linux-amd64");

    private final String fileName;
}
