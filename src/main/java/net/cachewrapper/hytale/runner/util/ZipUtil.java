package net.cachewrapper.hytale.runner.util;

import lombok.experimental.UtilityClass;
import net.cachewrapper.hytale.runner.HytaleRunnerPlugin;
import net.lingala.zip4j.ZipFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@UtilityClass
public final class ZipUtil {

    public void unpack(final @NotNull File zip, final @NotNull File projectDirectory) {
        final File serverDirectory = new File(projectDirectory, HytaleRunnerPlugin.HYTALE_SERVER_PATH);

        try (final ZipFile zipFile = new ZipFile(zip)) {
            zipFile.extractAll(serverDirectory.getAbsolutePath());
            zipFile.getFile().delete();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}