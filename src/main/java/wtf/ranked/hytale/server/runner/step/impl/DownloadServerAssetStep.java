package wtf.ranked.hytale.server.runner.step.impl;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.process.ExecOperations;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.extension.Patchline;
import wtf.ranked.hytale.server.runner.platform.PlatformFile;
import wtf.ranked.hytale.server.runner.platform.PlatformFileProvider;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;
import wtf.ranked.hytale.server.runner.util.FileUtil;

import javax.inject.Inject;
import java.io.File;

/**
 * Step responsible for downloading and extracting Hytale server assets.
 * <p>
 * This step identifies the host operating system via {@link PlatformFileProvider},
 * executes the native downloader binary to fetch the server software, and
 * extracts the resulting assets into the designated run directory.
 */
@NullMarked
public abstract class DownloadServerAssetStep extends TaskStepDefault {

    private static final String SERVER_ZIP_FILE = "hytale-server.zip";

    public DownloadServerAssetStep() {
        getRunDirectory().convention(getHytalePluginExtension().getRunDirectory());
        getPatchline().convention(getHytalePluginExtension().getPatchline());
        getAssets().convention(getHytalePluginExtension().getAssets());
    }

    @Override
    public final void runStep() {
        final File runDirectory = getRunDirectory().get().getAsFile();
        final PlatformFile platformFile = PlatformFileProvider.getPlatformFile();

        final File executableFile = new File(runDirectory, platformFile.getFileName());
        final File destinationZipFile = new File(runDirectory, SERVER_ZIP_FILE);
        final Patchline patchLine = getPatchline().get();

        getExecOperations().exec(execSpec -> {
            execSpec.setExecutable(executableFile);
            execSpec.workingDir(runDirectory);
            execSpec.args("-download-path", destinationZipFile.getAbsoluteFile());
            execSpec.args("-patchline", patchLine.getIdentifier());
        });

        getLogger().lifecycle("Unpacking hytale assets...");
        FileUtil.unpackZipFile(destinationZipFile, runDirectory);
        getLogger().lifecycle("Successfully unpacked downloaded hytale assets!");
    }

    @OutputDirectory
    protected abstract DirectoryProperty getRunDirectory();

    @OutputFile
    protected abstract RegularFileProperty getAssets();

    @Input
    protected abstract Property<Patchline> getPatchline();

    @Inject
    protected abstract ExecOperations getExecOperations();
}
