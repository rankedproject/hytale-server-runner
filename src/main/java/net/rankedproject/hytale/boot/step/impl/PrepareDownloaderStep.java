package net.rankedproject.hytale.boot.step.impl;

import net.rankedproject.hytale.boot.HytaleBootExtension;
import net.rankedproject.hytale.boot.resource.HttpResourceProvider;
import net.rankedproject.hytale.boot.step.Step;
import net.rankedproject.hytale.boot.step.type.StepDefault;
import net.rankedproject.hytale.boot.util.FileUtil;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.time.Duration;

public abstract class PrepareDownloaderStep extends StepDefault {

    @Override
    public @NotNull Step.Options options() {
        return Options.builder()
                .startStep(this::startStep)
                .build();
    }

    private void startStep() {
        final Project project = getProject();
        final HytaleBootExtension bootExtension = getHytaleBootExtension();

        if (bootExtension.getServerJar().get().exists() && bootExtension.getAssets().get().exists()) {
            setDidWork(false);
            return;
        }

        final File runDirectory = bootExtension.getRunDirectory().get().getAsFile();
        FileUtil.deleteDirectory(runDirectory);
        project.mkdir(runDirectory);

        final File destinationFile = new File(runDirectory, "hytale-downloader.zip");
        final URI serverDownloadUri = bootExtension.getServerDownloadUri().get();

        getResourceProvider().get().builder()
                .uri(serverDownloadUri)
                .timeout(Duration.ofSeconds(25))
                .destinationFile(destinationFile)
                .provide()
                .join();

        FileUtil.unpackZipFile(destinationFile, runDirectory);
    }

    @ServiceReference("httpResourceProvider")
    protected abstract @NotNull Property<HttpResourceProvider> getResourceProvider();
}
