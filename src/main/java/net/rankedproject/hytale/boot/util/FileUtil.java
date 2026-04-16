package net.rankedproject.hytale.boot.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@UtilityClass
public final class FileUtil {

    public void unpackZipFile(final @NotNull File zip, final @NotNull File destinationPath) {
        try (final ZipFile zipFile = new ZipFile(zip)) {
            zipFile.extractAll(destinationPath.getAbsolutePath());
            zipFile.getFile().delete();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SneakyThrows
    public void deleteFile(final @NotNull File directory, final @NotNull String fileName) {
        final File file = new File(directory, fileName);
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }

    public void deleteFiles(final @NotNull File directory, final @NotNull String... fileNames) {
        Arrays.asList(fileNames).forEach(fileName -> deleteFile(directory, fileName));
    }

    @SneakyThrows
    public void deleteDirectory(final @NotNull File directory) {
        if (directory.exists()) {
            FileUtils.forceDelete(directory);
        }
    }
}