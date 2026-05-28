package wtf.ranked.hytale.server.runner.step.impl;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.jspecify.annotations.NullMarked;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;
import wtf.ranked.hytale.server.runner.util.FileUtil;

import java.io.File;
import java.net.URI;
import java.time.Duration;

/**
 * Initial preparation step for acquiring Hytale server components.
 * <p>
 * This step manages the acquisition of the Hytale downloader utility. It downloads
 * the required archive from a remote URI, validates it within the specified
 * timeout, and extracts its contents into the server run directory.
 */
@NullMarked
public abstract class PrepareDownloaderStep extends TaskStepDefault {

    public PrepareDownloaderStep() {
        getRunDirectory().convention(getHytalePluginExtension().getRunDirectory());
        getDownloadTimeout().convention(getHytalePluginExtension().getDownloadTimeout());
        getServerDownloadUri().convention(getHytalePluginExtension().getServerDownloadUri());
    }

    @Override
    public final void runStep() {
        final File runDirectory = getRunDirectory().get().getAsFile();
        final File destinationZipFile = new File(runDirectory, "hytale-downloader.zip");

        getLogger().lifecycle("Downloading hytale downloading files...");
        getResourceProvider().get().builder()
                .uri(getServerDownloadUri().get())
                .timeout(getDownloadTimeout().get())
                .destinationFile(destinationZipFile)
                .provide();
        getLogger().lifecycle("Successfully downloaded hytale downloading files!");

        getLogger().lifecycle("Unpacking downloading files...");
        FileUtil.unpackZipFile(destinationZipFile, runDirectory);
        getLogger().lifecycle("Successfully unpacked downloading files!");
    }

    @Input
    protected abstract Property<URI> getServerDownloadUri();

    @Input
    protected abstract Property<Duration> getDownloadTimeout();

    @OutputDirectory
    protected abstract DirectoryProperty getRunDirectory();

    @ServiceReference("httpResourceProvider")
    protected abstract Property<HttpResourceProvider> getResourceProvider();
}
