package wtf.ranked.hytale.server.runner.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Utility class for file and directory operations.
 * <p>
 * Provides helper methods for extracting archives and managing
 * the cleanup of server files and directories.
 */
@UtilityClass
@SuppressWarnings("UnusedReturnValue")
public final class FileUtil {

    /**
     * Extracts all contents of a ZIP archive to the specified destination and
     * subsequently deletes the source archive file.
     *
     * @param zip             the source ZIP archive file to extract
     * @param destinationPath the target directory where contents will be extracted
     * @throws RuntimeException if an I/O error occurs during extraction or deletion
     */
    public void unpackZipFile(final @NonNull File zip, final @NonNull File destinationPath) {
        try (ZipFile zipFile = new ZipFile(zip)) {
            zipFile.extractAll(destinationPath.getAbsolutePath());
            Files.delete(zipFile.getFile().toPath());
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Safely deletes a file or directory recursively.
     * <p>
     * If the specified file or directory does not exist, no action is taken.
     * Otherwise, it forces the deletion of the target and its contents.
     *
     * @param file the file or directory to delete
     */
    @SneakyThrows
    public void deleteFile(final @NonNull File file) {
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }
}
