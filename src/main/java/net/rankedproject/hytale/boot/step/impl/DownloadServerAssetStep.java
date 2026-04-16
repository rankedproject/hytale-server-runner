package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.step.TaskStep;
import net.rankedproject.hytale.boot.step.type.TaskStepDefault;
import net.rankedproject.hytale.boot.util.FileUtil;
import org.apache.commons.lang3.SystemUtils;
import org.gradle.process.ExecOperations;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.File;

/**
 * Step that executes the native Hytale downloader binary.
 * <p>
 * Identifies the host OS to run the correct executable and
 * extracts the resulting server assets into the run directory.
 */
public abstract class DownloadServerAssetStep extends TaskStepDefault {

    private static final String SERVER_ZIP_FILE = "hytale-server.zip";

    private static final String WINDOWS_EXECUTABLE_FILE = "hytale-downloader-windows-amd64.exe";
    private static final String LINUX_EXECUTABLE_FILE = "hytale-downloader-linux-amd64";

    @Override
    public final @NotNull TaskStep.Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    private void startStep() {
        final HytaleBootExtension bootExtension = getHytaleBootExtension();
        if (bootExtension.getServerJar().get().exists() && bootExtension.getAssets().get().exists()) {
            setDidWork(false);
            return;
        }

        final File runDirectory = bootExtension.getRunDirectory().get().getAsFile();
        final String executableFileName = SystemUtils.IS_OS_WINDOWS
                ? WINDOWS_EXECUTABLE_FILE
                : LINUX_EXECUTABLE_FILE;

        final File executableFile = new File(runDirectory, executableFileName);
        final File destinationZipFile = new File(runDirectory, SERVER_ZIP_FILE);

        getExecOperations().exec(execSpec -> {
            execSpec.setExecutable(executableFile);
            execSpec.workingDir(runDirectory);
            execSpec.args("-download-path", destinationZipFile.getAbsoluteFile());
        });

        FileUtil.unpackZipFile(destinationZipFile, runDirectory);
    }

    @Inject
    protected abstract @NotNull ExecOperations getExecOperations();
}
