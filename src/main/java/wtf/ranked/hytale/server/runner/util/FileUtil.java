package wtf.ranked.hytale.server.runner.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Utility class for file and directory operations.
 * <p>
 * Provides helper methods for extracting archives and managing
 * the cleanup of server files and directories.
 */
@UtilityClass
public final class FileUtil {

    /**
     * Extracts a ZIP file to the specified destination and deletes the source archive.
     *
     * @param zip             source zip file
     * @param destinationPath directory to extract files into
     */
    public void unpackZipFile(final @NonNull File zip, final @NonNull File destinationPath) {
        try (ZipFile zipFile = new ZipFile(zip)) {
            zipFile.extractAll(destinationPath.getAbsolutePath());
            zipFile.getFile().delete();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SneakyThrows
    public void deleteFile(final @NonNull File directory, final @NonNull String fileName) {
        final File file = new File(directory, fileName);
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }

    @SneakyThrows
    public void deleteFile(final @NonNull File file) {
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }

    /**
     * Deletes multiple files within a directory by name.
     *
     * @param directory parent directory
     * @param fileNames names of the files to remove
     */
    public void deleteFiles(final @NonNull File directory, final @NonNull String... fileNames) {
        Arrays.asList(fileNames).forEach(fileName -> deleteFile(directory, fileName));
    }

    /**
     * Recursively deletes a directory and all of its contents.
     *
     * @param directory directory to remove
     */
    @SneakyThrows
    public void deleteDirectory(final @NonNull File directory) {
        if (directory.exists()) {
            FileUtils.forceDelete(directory);
        }
    }
}
