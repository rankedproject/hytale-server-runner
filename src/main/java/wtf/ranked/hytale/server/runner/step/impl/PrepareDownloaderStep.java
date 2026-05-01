package wtf.ranked.hytale.server.runner.step.impl;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jspecify.annotations.NonNull;
import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;
import wtf.ranked.hytale.server.runner.util.FileUtil;

import java.io.File;
import java.net.URI;

/**
 * Initial preparation step for acquiring Hytale server components.
 * <p>
 * Downloads and extracts the core downloader utility if the
 * server software is not already present.
 */
public abstract class PrepareDownloaderStep extends TaskStepDefault {

    @Override
    public void runStep() {
        final Project project = getProject();
        final HytalePluginExtension pluginExtension = getHytalePluginExtension();

        if (pluginExtension.getServerJar().get().exists() && pluginExtension.getAssets().get().exists()) {
            setDidWork(false);
            return;
        }

        final File runDirectory = pluginExtension.getRunDirectory().get().getAsFile();
        FileUtil.deleteDirectory(runDirectory);
        project.mkdir(runDirectory);

        final File destinationZipFile = new File(runDirectory, "hytale-downloader.zip");
        final URI serverFilesDownloadUri = pluginExtension.getServerDownloadUri().get();

        getResourceProvider().get().builder()
                .uri(serverFilesDownloadUri)
                .timeout(pluginExtension.getDownloadTimeout().get())
                .destinationFile(destinationZipFile)
                .provide();

        FileUtil.unpackZipFile(destinationZipFile, runDirectory);
    }

    @ServiceReference("httpResourceProvider")
    protected abstract @NonNull Property<HttpResourceProvider> getResourceProvider();
}
