package wtf.ranked.hytale.server.runner.step.impl;

import wtf.ranked.hytale.server.runner.HytalePluginExtension;
import wtf.ranked.hytale.server.runner.resource.HttpResourceProvider;
import wtf.ranked.hytale.server.runner.step.type.TaskStepDefault;
import wtf.ranked.hytale.server.runner.util.FileUtil;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jspecify.annotations.NonNull;

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
    public final @NonNull Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    private void startStep() {
        final Project project = getProject();
        final HytalePluginExtension pluginExtension = getHytalePluginExtension();

        if (pluginExtension.getServerJar().get().exists() && pluginExtension.getAssets().get().exists()) {
            setDidWork(false);
            return;
        }

        final File runDirectory = pluginExtension.getRunDirectory().get().getAsFile();
        FileUtil.deleteDirectory(runDirectory);
        project.mkdir(runDirectory);

        final File destinationFile = new File(runDirectory, "hytale-downloader.zip");
        final URI serverDownloadUri = pluginExtension.getServerDownloadUri().get();

        getResourceProvider().get().builder()
                .uri(serverDownloadUri)
                .timeout(pluginExtension.getDownloadTimeout().get())
                .destinationFile(destinationFile)
                .provide();

        FileUtil.unpackZipFile(destinationFile, runDirectory);
    }

    @ServiceReference("httpResourceProvider")
    protected abstract @NonNull Property<HttpResourceProvider> getResourceProvider();
}
