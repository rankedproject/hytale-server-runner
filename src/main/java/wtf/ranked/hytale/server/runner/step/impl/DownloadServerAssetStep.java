package wtf.ranked.hytale.server.runner.step.impl;

import org.gradle.process.ExecOperations;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.platform.PlatformFile;
import wtf.ranked.hytale.server.runner.platform.PlatformFileProvider;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;
import wtf.ranked.hytale.server.runner.util.FileUtil;

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

    @Override
    public void runStep() {
        final HytalePluginExtension pluginExtension = getHytalePluginExtension();
        if (pluginExtension.getServerJar().get().exists() && pluginExtension.getAssets().get().exists()) {
            setDidWork(false);
            return;
        }

        final File runDirectory = pluginExtension.getRunDirectory().get().getAsFile();
        final PlatformFile platformFile = PlatformFileProvider.getPlatformFile();

        final File executableFile = new File(runDirectory, platformFile.getFileName());
        final File destinationZipFile = new File(runDirectory, SERVER_ZIP_FILE);

        getExecOperations().exec(execSpec -> {
            execSpec.setExecutable(executableFile);
            execSpec.workingDir(runDirectory);
            execSpec.args("-download-path", destinationZipFile.getAbsoluteFile());
        });

        FileUtil.unpackZipFile(destinationZipFile, runDirectory);
    }

    @Inject
    protected abstract @NonNull ExecOperations getExecOperations();
}
