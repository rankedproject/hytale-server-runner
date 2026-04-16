package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.type.StepDefault;
import net.rankedproject.hytale.boot.util.FileUtil;
import org.apache.commons.lang3.SystemUtils;
import org.gradle.process.ExecOperations;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.File;

public abstract class DownloadServerAssetStep extends StepDefault {

    private static final String SERVER_ZIP_FILE = "hytale-server.zip";

    private static final String WINDOWS_EXECUTABLE_FILE = "hytale-downloader-windows-amd64.exe";
    private static final String LINUX_EXECUTABLE_FILE = "hytale-downloader-linux-amd64";

    @Override
    public @NotNull Step.Options options() {
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

        this.getExecOperations().exec(execSpec -> {
            execSpec.setExecutable(executableFile);
            execSpec.workingDir(runDirectory);
            execSpec.args("-download-path", destinationZipFile.getAbsoluteFile());
        });

        FileUtil.unpackZipFile(destinationZipFile, runDirectory);
    }

    @Inject
    protected abstract @NotNull ExecOperations getExecOperations();
}
