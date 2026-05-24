package wtf.ranked.hytale.server.runner.platform;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SystemUtils;
import org.jspecify.annotations.NullMarked;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Provides the appropriate platform-specific file configuration based on the host operating system.
 */
@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlatformFileProvider {

    private static final String PLATFORM_ERROR_MESSAGE;

    private static final Map<BooleanSupplier, PlatformFile> PLATFORM_MAP = new LinkedHashMap<>();

    static {
        PLATFORM_ERROR_MESSAGE = "Platform not found. It seems your OS is not supported. Please get in touch with us on GitHub!";
        PLATFORM_MAP.put(() -> SystemUtils.IS_OS_WINDOWS, PlatformFile.WINDOWS);
        PLATFORM_MAP.put(() -> SystemUtils.IS_OS_LINUX, PlatformFile.LINUX);
    }

    /**
     * Detects the current operating system and returns its corresponding platform file.
     *
     * @return the platform file matching the current OS
     * @throws NullPointerException if the current operating system is not supported
     */
    public static PlatformFile getPlatformFile() {
        final PlatformFile platformFile = PLATFORM_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().getAsBoolean())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        Preconditions.checkNotNull(platformFile, PLATFORM_ERROR_MESSAGE);
        return platformFile;
    }
}
